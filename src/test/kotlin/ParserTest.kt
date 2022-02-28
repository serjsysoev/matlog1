import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ParserTest {
    @Test
    fun `toTokenTree a`() {
        "a".toTokenTree().deepEquals(variableA)
    }

    @Test
    fun `toTokenTree ~a`() {
        "~a".toTokenTree().deepEquals(negationA)
    }

    @Test
    fun `toTokenTree ~a & b`() {
        "~a & b".toTokenTree().deepEquals(conjunction)
    }

    @Test
    fun `toTokenTree ~a or b`() {
        "~a | b".toTokenTree().deepEquals(disjunction)
    }

    @Test
    fun `toTokenTree ~(~a & b)`() {
        "~(~a & b)".toTokenTree().deepEquals(negativeConjunction)
    }

    @Test
    fun `toTokenTree ~(~a or b)`() {
        "~(~a | b)".toTokenTree().deepEquals(negativeDisjunction)
    }

    @Test
    fun `toTokenTree ~(~a & b) & ~(~a or b)`() {
        "~(~a & b) & ~(~a | b)".toTokenTree().deepEquals(bigConjunction)
    }

    @Test
    fun `toTokenTree ~(~a & b) or ~(~a or b)`() {
        "~(~a & b) | ~(~a | b)".toTokenTree().deepEquals(bigDisjunction)
    }

    @Test
    fun `toTokenTree spaces`() {
        val test = "~ (~ a &   b) | ~ ( ~   ((a))  | b)"
        test.toTokenTree().deepEquals(test.replace(" ", "").toTokenTree())
    }
}