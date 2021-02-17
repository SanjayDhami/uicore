package utils
import javax.crypto.Cipher
import java.util.Base64.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class StringEncrypt {
    private val secretKey:String = ".[QvfRw)VWpBEnM3`82+}38/"
    private fun cipher(opcode:Int):Cipher{
        if(secretKey.length < 20) throw RuntimeException("SecretKey length is not 20 chars")
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opcode, sk, iv)
        return c
    }
    fun encrypt(str:String):String{
        val encrypted = cipher(Cipher.ENCRYPT_MODE).doFinal(str.toByteArray(Charsets.UTF_8))
        return String(getEncoder().encode(encrypted))
    }
    fun decrypt(str:String):String{
        val byteStr = getDecoder().decode(str.toByteArray(Charsets.UTF_8))
        return String(cipher(Cipher.DECRYPT_MODE).doFinal(byteStr))
    }

}

