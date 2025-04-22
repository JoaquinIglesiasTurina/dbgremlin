import org.rogach.scallop._

class Conf(argumens: Seq[String]) extends ScallopConf(argumens):
    val userEmail = opt[String](required = true)
    val jobId = opt[Long](required = true)
    verify()

def main(args: Array[String]) =
    val conf = new Conf(args)
    val userEmail = conf.userEmail()
    val jobId = conf.jobId()
    println(
        s"giving ${userEmail} permissions in job ${jobId}")
    givePermissions(userEmail, jobId)