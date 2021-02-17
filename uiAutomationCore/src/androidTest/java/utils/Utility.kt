package utils

class Utility {
   fun generateRandomText(): String {
       val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
       return (1..10)
           .map { charset.random() }
           .joinToString("")
    }
}