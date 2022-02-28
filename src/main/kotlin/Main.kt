fun main() {
    println("Syntax: `&` - and, `|` - or, `~` - not. Variables can consist of letters and digits. You can use brackets")
    print("Enter boolean formula: ")
    val formula = readln().toTokenTree().normalized()

    print("NNF: ")
    formula.toNegationNormalForm().println()
    print("DNF: ")
    formula.toDisjunctiveNormalForm().println()
    print("CNF: ")
    formula.toConjunctiveNormalForm().println()
}
