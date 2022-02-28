import Token.*
import kotlin.math.max

fun Token.toNegationNormalForm(): Token = when (this) {
    is Negation -> when (child) {
        is Negation -> child.child.toNegationNormalForm()
        is Conjunction -> Disjunction(child.children.map { Negation(it) }).toNegationNormalForm()
        is Disjunction -> Conjunction(child.children.map { Negation(it) }).toNegationNormalForm()
        is Variable -> this
    }
    is Conjunction -> Conjunction(children.map { it.toNegationNormalForm() })
    is Disjunction -> Disjunction(children.map { it.toNegationNormalForm() })
    is Variable -> this
}

fun Token.toDisjunctiveNormalForm(): Token {
    fun Token.toDisjunctiveNormalFormInternal(): List<Token> = when (this) {
        is Conjunction -> cartesianProduct(children.map { it.toDisjunctiveNormalFormInternal() }).map { Conjunction(it) }
        is Disjunction -> children.flatMap { it.toDisjunctiveNormalFormInternal() }
        else -> listOf(this)
    }

    val conjunctions = toNegationNormalForm().toDisjunctiveNormalFormInternal()
    return if (conjunctions.size > 1) Disjunction(conjunctions) else conjunctions.first()
}

fun Token.toConjunctiveNormalForm(): Token {
    val variableNamePrefix = "temp"
    var count = 0

    // if there is a variable with a name "test...", we want to make sure there will be no clashes
    dfs { token, _: List<Unit> ->
        if (token is Variable && token.variableName.startsWith(variableNamePrefix)) {
            token.variableName.drop(variableNamePrefix.length).toIntOrNull()?.let {
                count = max(count, it + 1)
            }
        }
    }
    // now we know, that $variableNamePrefix$count, $variableNamePrefix${count + 1}, ... won't clash with variables in the formula

    val disjunctions = mutableListOf<Token>()

    val rootVariable = dfs { token, childrenVariables: List<Variable> ->
        val newVariable = Variable("$variableNamePrefix${count++}")
        val newDisjunctions = when (token) {
            is Negation -> listOf(
                Disjunction(childrenVariables + newVariable),
                Disjunction(childrenVariables.map { Negation(it) } + Negation(newVariable)),
            )
            is Conjunction -> childrenVariables.map {
                Disjunction(it, Negation(newVariable))
            } + Disjunction(childrenVariables.map { Negation(it) } + newVariable)
            is Disjunction -> childrenVariables.map {
                Disjunction(Negation(it), newVariable)
            } + Disjunction(childrenVariables + Negation(newVariable))
            is Variable -> return@dfs token
        }
        disjunctions.addAll(newDisjunctions)
        return@dfs newVariable
    }
    disjunctions.add(rootVariable)

    return if (disjunctions.size > 1) Conjunction(disjunctions) else disjunctions.first()
}

private fun <T> cartesianProduct(listOfLists: List<List<T>>): List<List<T>> =
    listOfLists.fold(listOf(listOf())) { acc, list ->
        acc.flatMap { accList -> list.map { element -> accList + element } }
    }

/**
 * Visits a token only after all the children were visited
 */
private fun <T> Token.dfs(block: (Token, List<T>) -> T): T {
    val result = children.map { it.dfs(block) }
    return block(this, result)
}
