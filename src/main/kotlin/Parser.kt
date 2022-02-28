sealed class Token(val children: List<Token>) {

    data class Variable(val variableName: String) : Token(emptyList()) {
        override fun print() = print(variableName)
        override fun normalized(): Token = this
    }

    class Negation(val child: Token) : Token(listOf(child)) {
        override fun print() {
            print("~")
            child.printWithParentheses()
        }

        override fun normalized() =
            if (child is Negation) {
                child.child.normalized()
            } else {
                Negation(child.normalized())
            }
    }

    class Conjunction(children: List<Token>) : Token(children) {
        constructor(vararg children: Token) : this(children.toList())

        override fun print() {
            children.forEachIndexed { index, child ->
                child.printWithParentheses()
                if (index + 1 != children.size) {
                    print(" & ")
                }
            }
        }

        override fun normalized() = Conjunction(children.map { it.normalized() })
    }

    class Disjunction(children: List<Token>) : Token(children.toList()) {
        constructor(vararg children: Token) : this(children.toList())

        override fun print() {
            children.forEachIndexed { index, child ->
                child.printWithParentheses()
                if (index + 1 != children.size) {
                    print(" | ")
                }
            }
        }

        override fun normalized() = Disjunction(children.map { it.normalized() })
    }

    abstract fun print()

    fun println() {
        print()
        println("")
    }

    protected fun printWithParentheses() {
        if (this is Variable || this is Negation) {
            print()
        } else {
            print("(")
            print()
            print(")")
        }
    }

    abstract fun normalized(): Token
}

private val operators = listOf('|', '&', '~')  // listed in order of priority (ascending)

private fun getOperatorPriority(operator: Char): Int {
    return operators.indexOf(operator)
}

fun String.toTokenTree(): Token {
    val tokens = mutableListOf<Token>()
    val nonVariables = mutableListOf<Char>()

    fun processOperator(operator: Char) {
        val newToken = if (operator == '~') {
            Token.Negation(tokens.removeLast())
        } else {
            val rightChild = tokens.removeLast()
            val leftChild = tokens.removeLast()

            if (operator == '&') {
                Token.Conjunction(leftChild, rightChild)
            } else {
                Token.Disjunction(leftChild, rightChild)
            }
        }
        tokens.add(newToken)
    }

    val currentToken = StringBuilder()
    forEach {
        if (!it.isLetterOrDigit() && currentToken.isNotEmpty()) {
            tokens.add(Token.Variable(currentToken.toString()))
            currentToken.clear()
        }
        when (it) {
            ' ' -> return@forEach
            '(' -> nonVariables.add(it)
            ')' -> {
                while (nonVariables.last() != '(') {
                    processOperator(nonVariables.removeLast())
                }
                nonVariables.removeLast()
            }
            in operators -> {
                while (nonVariables.isNotEmpty()
                    // if operator priority on stack is higher we need to process it
                    && (getOperatorPriority(nonVariables.last()) > getOperatorPriority(it)
                            // if operator is left-associative we need to process it in case of priority equality
                            || (it != '~' && getOperatorPriority(nonVariables.last()) == getOperatorPriority(it)))
                ) {
                    processOperator(nonVariables.removeLast())
                }
                nonVariables.add(it)
            }
            else -> currentToken.append(it)
        }
    }
    if (currentToken.isNotEmpty()) {
        tokens.add(Token.Variable(currentToken.toString()))
        currentToken.clear()
    }
    while (nonVariables.isNotEmpty()) {
        processOperator(nonVariables.removeLast())
    }
    check(tokens.size == 1)
    return tokens.first()
}
