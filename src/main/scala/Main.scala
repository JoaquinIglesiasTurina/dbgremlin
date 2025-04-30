import org.rogach.scallop._

class Conf(argumens: Seq[String]) extends ScallopConf(argumens):
    object GivePermissionsConf extends Subcommand("give-job-permissions"):
        val userEmail = opt[String](required = true)
        val jobId = opt[Long](required = true)
    addSubcommand(GivePermissionsConf)
    verify()

class MainFunctions:
    def givePermissions(userEmail: String, jobId: Long) =
        GivePermissions.givePermissions(userEmail, jobId)

def mainWithFunctions(
    args: Seq[String], 
    mainFunctions: MainFunctions
): Unit =
    val conf = new Conf(args)
    conf.subcommand match
        case Some(conf.GivePermissionsConf) =>     
            val userEmail = conf.GivePermissionsConf.userEmail()
            val jobId = conf.GivePermissionsConf.jobId()
            println(s"giving ${userEmail} permissions in job ${jobId}")
            mainFunctions.givePermissions(userEmail, jobId)
        case Some(_) => println("Subcommand not implemented")
        case None => println("Invalid subcommand")

object Main:
    def main(args: Array[String]): Unit = 
        mainWithFunctions(args, MainFunctions())