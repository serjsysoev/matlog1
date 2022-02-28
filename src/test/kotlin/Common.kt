import Token.*

val variableA = Variable("a")
val variableB = Variable("b")
val negationA = Negation(variableA)
val conjunction = Conjunction(negationA, variableB)
val disjunction = Disjunction(negationA, variableB)
val negativeConjunction = Negation(conjunction)
val negativeDisjunction = Negation(disjunction)
val bigConjunction = Conjunction(negativeConjunction, negativeDisjunction)
val bigDisjunction = Disjunction(negativeConjunction, negativeDisjunction)

fun Token.deepEquals(other: Token): Boolean =
    if (this is Variable && other is Variable) {
        variableName == other.variableName
    } else {
        this::class == other::class
                && children.size == other.children.size
                && children.mapIndexed { index, token -> token.deepEquals(other.children[index]) }.all { it }
    }
