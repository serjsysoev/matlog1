import org.junit.jupiter.api.Test

import Token.*

internal class TransformersTest {
    @Test
    fun `toNegationNormalForm a`() {
        assert(variableA.toNegationNormalForm().deepEquals(variableA))
    }

    @Test
    fun `toNegationNormalForm ~a`() {
        assert(negationA.toNegationNormalForm().deepEquals(negationA))
    }

    @Test
    fun `toNegationNormalForm ~a & b`() {
        assert(conjunction.toNegationNormalForm().deepEquals(conjunction))
    }

    @Test
    fun `toNegationNormalForm ~a or b`() {
        assert(disjunction.toNegationNormalForm().deepEquals(disjunction))
    }

    @Test
    fun `toNegationNormalForm ~(~a & b)`() {
        assert(
            negativeConjunction.toNegationNormalForm().deepEquals(
                Disjunction(variableA, Negation(variableB))
            )
        )
    }

    @Test
    fun `toNegationNormalForm ~(~a or b)`() {
        assert(
            negativeDisjunction.toNegationNormalForm().deepEquals(
                Conjunction(variableA, Negation(variableB))
            )
        )
    }

    @Test
    fun `toNegationNormalForm ~(~a & b) & ~(~a or b)`() {
        assert(
            bigConjunction.toNegationNormalForm().deepEquals(
                Conjunction(Disjunction(variableA, Negation(variableB)), Conjunction(variableA, Negation(variableB)))
            )
        )
    }

    @Test
    fun `toNegationNormalForm ~(~a & b) or ~(~a or b)`() {
        assert(
            bigDisjunction.toNegationNormalForm().deepEquals(
                Disjunction(Disjunction(variableA, Negation(variableB)), Conjunction(variableA, Negation(variableB)))
            )
        )
    }

    @Test
    fun `toDisjunctiveNormalForm a`() {
        assert(variableA.toDisjunctiveNormalForm().deepEquals(variableA))
    }

    @Test
    fun `toDisjunctiveNormalForm ~a`() {
        assert(negationA.toDisjunctiveNormalForm().deepEquals(negationA))
    }

    @Test
    fun `toDisjunctiveNormalForm ~a & b`() {
        assert(conjunction.toDisjunctiveNormalForm().deepEquals(conjunction))
    }

    @Test
    fun `toDisjunctiveNormalForm ~a or b`() {
        assert(disjunction.toDisjunctiveNormalForm().deepEquals(disjunction))
    }

    @Test
    fun `toDisjunctiveNormalForm ~(~a & b)`() {
        assert(
            negativeConjunction.toDisjunctiveNormalForm().deepEquals(
                Disjunction(variableA, Negation(variableB))
            )
        )
    }

    @Test
    fun `toDisjunctiveNormalForm ~(~a or b)`() {
        assert(
            negativeDisjunction.toDisjunctiveNormalForm().deepEquals(
                Conjunction(variableA, Negation(variableB))
            )
        )
    }

    @Test
    fun `toDisjunctiveNormalForm ~(~a & b) & ~(~a or b)`() {
        assert(
            bigConjunction.toDisjunctiveNormalForm().deepEquals(
                Disjunction(
                    Conjunction(variableA, Conjunction(variableA, Negation(variableB))),
                    Conjunction(Negation(variableB), Conjunction(variableA, Negation(variableB)))
                )
            )
        )
    }

    @Test
    fun `toDisjunctiveNormalForm ~(~a & b) or ~(~a or b)`() {
        assert(
            bigDisjunction.toDisjunctiveNormalForm().deepEquals(
                Disjunction(variableA, Negation(variableB), Conjunction(variableA, Negation(variableB)))
            )
        )
    }

    val temp1 = Variable("temp1")
    val temp2 = Variable("temp2")
    val temp3 = Variable("temp3")
    val temp4 = Variable("temp4")

    @Test
    fun `toConjunctiveNormalForm a`() {
        assert(variableA.toConjunctiveNormalForm().deepEquals(variableA))
    }

    @Test
    fun `toConjunctiveNormalForm ~a`() {
        assert(
            negationA.toConjunctiveNormalForm().deepEquals(
                Conjunction(Disjunction(variableA, temp1), Disjunction(negationA, Negation(temp1)), temp1)
            )
        )
    }

    @Test
    fun `toConjunctiveNormalForm ~a & b`() {
        assert(
            conjunction.toConjunctiveNormalForm().deepEquals(
                Conjunction(
                    Disjunction(variableA, temp1),
                    Disjunction(negationA, Negation(temp1)),
                    Disjunction(temp1, Negation(temp3)),
                    Disjunction(variableB, Negation(temp3)),
                    Disjunction(Negation(temp1), Negation(variableB), temp3),
                    temp3
                )
            )
        )
    }

    @Test
    fun `toConjunctiveNormalForm ~a or b`() {
        assert(
            disjunction.toConjunctiveNormalForm().deepEquals(
                Conjunction(
                    Disjunction(variableA, temp1),
                    Disjunction(negationA, Negation(temp1)),
                    Disjunction(Negation(temp1), temp3),
                    Disjunction(Negation(variableB), temp3),
                    Disjunction(temp1, variableB, Negation(temp3)),
                    temp3
                )
            )
        )
    }

    @Test
    fun `toConjunctiveNormalForm ~(~a & b)`() {
        assert(
            negativeConjunction.toConjunctiveNormalForm().deepEquals(
                Conjunction(
                    Disjunction(variableA, temp1),
                    Disjunction(negationA, Negation(temp1)),
                    Disjunction(temp1, Negation(temp3)),
                    Disjunction(variableB, Negation(temp3)),
                    Disjunction(Negation(temp1), Negation(variableB), temp3),
                    Disjunction(temp3, temp4),
                    Disjunction(Negation(temp3), Negation(temp4)),
                    temp4,
                )
            )
        )
    }

    @Test
    fun `toConjunctiveNormalForm ~(~a or b)`() {
        assert(
            negativeDisjunction.toConjunctiveNormalForm().deepEquals(
                Conjunction(
                    Disjunction(variableA, temp1),
                    Disjunction(negationA, Negation(temp1)),
                    Disjunction(Negation(temp1), temp3),
                    Disjunction(Negation(variableB), temp3),
                    Disjunction(temp1, variableB, Negation(temp3)),
                    Disjunction(temp3, temp4),
                    Disjunction(Negation(temp3), Negation(temp4)),
                    temp4,
                )
            )
        )
    }
}