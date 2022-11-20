private fun isTicketWinning(number: Int): Boolean {
    val newNumber = number.toString().map { digit -> digit.digitToInt() }
    val size = newNumber.size
    var sum = 0
    for(i in 0 until size / 2) {
        sum += newNumber[i] - newNumber[size - 1 - i]
    }
    return sum == 0
}