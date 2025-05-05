import org.rogach.scallop._
import com.databricks.sdk.service.jobs.JobPermissionLevel

class Conf(argumens: Seq[String]) extends ScallopConf(argumens):
  object GivePermissionsConf extends Subcommand("give-job-permissions"):
    val userEmail = opt[String](required = true)
    val jobId = opt[Long](required = true)
        val permissionLevel = choice(
            Seq("CAN_MANAGE_RUN", "CAN_MANAGE", "CAN_VIEW"),
            default = Some("CAN_MANAGE_RUN")
        )
  addSubcommand(GivePermissionsConf)
  object ListDependentConf extends Subcommand("list-dependent"):
    val jobId = opt[Long](required = true)
  addSubcommand(ListDependentConf)
  verify()

class MainFunctions:
  def givePermissions(
        userEmail: String, 
        jobId: Long, 
        permissionLevel: JobPermissionLevel
    ): Unit =
    GivePermissions.givePermissions(userEmail, jobId)
  def listDependent(jobId: Long) =
    ListDependent.listDependent(jobId)

def mainWithFunctions(
  args: Seq[String], 
  mainFunctions: MainFunctions
): Unit =
  val conf = new Conf(args)
  conf.subcommand match
    case Some(conf.GivePermissionsConf) =>     
      val userEmail = conf.GivePermissionsConf.userEmail()
      val jobId = conf.GivePermissionsConf.jobId()
            val permissionLevel = JobPermissionLevel.valueOf(
                conf.GivePermissionsConf.permissionLevel()
            )
      println(s"giving ${userEmail} permissions in job ${jobId}")
      mainFunctions.givePermissions(userEmail, jobId, permissionLevel)
    case Some(conf.ListDependentConf) =>
      val jobId = conf.GivePermissionsConf.jobId()
      mainFunctions.listDependent(jobId)
    case Some(_) => new Exception("Subcommand not implemented")
    case None => new Exception("Invalid subcommand")

object Main:
    def main(args: Array[String]): Unit = 
        mainWithFunctions(args, MainFunctions())