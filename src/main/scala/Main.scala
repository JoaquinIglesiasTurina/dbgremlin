import org.rogach.scallop._

class Conf(argumens: Seq[String]) extends ScallopConf(argumens):
    val userEmail = opt[String](required = true)
    val jobId = opt[Long](required = true)
    verify()

class MainFunctions:
    def givePermissions(userEmail: String, jobId: Long) =
        GivePermissions.givePermissions(userEmail, jobId)

def mainWithFunctions(
    args: Array[String], 
    mainFunctions: MainFunctions
) =
    val conf = new Conf(args)
    val userEmail = conf.userEmail()
    val jobId = conf.jobId()
    println(
        s"giving ${userEmail} permissions in job ${jobId}")
    mainFunctions.givePermissions(userEmail, jobId)

def main(args: Array[String]) = mainWithFunctions(args, MainFunctions())