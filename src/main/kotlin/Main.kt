fun main() {
    val formula = readln().toTokenTree().normalized()
    formula.toNegationNormalForm().println()
    formula.toDisjunctiveNormalForm().println()
    formula.toConjunctiveNormalForm().println()
}
