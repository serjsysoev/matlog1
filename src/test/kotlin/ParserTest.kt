import org.junit.jupiter.api.Test

internal class ParserTest {
    @Test
    fun `toTokenTree a`() {
        assert("a".toTokenTree().deepEquals(variableA))
    }

    @Test
    fun `toTokenTree ~a`() {
        assert("~a".toTokenTree().deepEquals(negationA))
    }

    @Test
    fun `toTokenTree ~a & b`() {
        assert("~a & b".toTokenTree().deepEquals(conjunction))
    }

    @Test
    fun `toTokenTree ~a or b`() {
        assert("~a | b".toTokenTree().deepEquals(disjunction))
    }

    @Test
    fun `toTokenTree ~(~a & b)`() {
        assert("~(~a & b)".toTokenTree().deepEquals(negativeConjunction))
    }

    @Test
    fun `toTokenTree ~(~a or b)`() {
        assert("~(~a | b)".toTokenTree().deepEquals(negativeDisjunction))
    }

    @Test
    fun `toTokenTree ~(~a & b) & ~(~a or b)`() {
        assert("~(~a & b) & ~(~a | b)".toTokenTree().deepEquals(bigConjunction))
    }

    @Test
    fun `toTokenTree ~(~a & b) or ~(~a or b)`() {
        assert("~(~a & b) | ~(~a | b)".toTokenTree().deepEquals(bigDisjunction))
    }

    @Test
    fun `toTokenTree spaces`() {
        val test = "~ (~ a &   b) | ~ ( ~   ((a))  | b)"
        assert(test.toTokenTree().deepEquals(test.replace(" ", "").toTokenTree()))
    }
}