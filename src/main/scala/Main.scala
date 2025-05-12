import org.rogach.scallop._
import com.databricks.sdk.service.jobs.JobPermissionLevel
import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.core.DatabricksConfig

class Conf(argumens: Seq[String]) extends ScallopConf(argumens):
  trait CommonOpts extends ScallopConf:
    val jobId = opt[Long](required = true)
    val profile = opt[String](default = Some("DEFAULT"))

  object GivePermissionsConf extends Subcommand("give-job-permissions"), CommonOpts:
    val userEmail = opt[String](required = true)
    val permissionLevel = choice(
      Seq("CAN_MANAGE_RUN", "CAN_MANAGE", "CAN_VIEW"),
      default = Some("CAN_MANAGE"))
  addSubcommand(GivePermissionsConf)

  object ListDependentConf extends Subcommand("list-dependent"), CommonOpts
  addSubcommand(ListDependentConf)
  verify()

class MainFunctions:
  def givePermissions(
    userEmail: String, 
    jobId: Long,
    permissionLevel: JobPermissionLevel,
    workspaceClient: WorkspaceClient
  ): Unit =
    GivePermissions
      .givePermissions(userEmail, jobId, permissionLevel, workspaceClient)
  def listDependent(jobId: Long, workspaceClient: WorkspaceClient) = 
    ListDependent.listDependent(jobId, workspaceClient)

def mainWithFunctions(args: Seq[String], mainFunctions: MainFunctions): Unit =
  val conf = new Conf(args)
  conf.subcommand match
    case Some(conf.GivePermissionsConf) =>
      val databricksConfig = DatabricksConfig()
        .setProfile(conf.GivePermissionsConf.profile())
      val workspaceClient = WorkspaceClient(databricksConfig)
      val userEmail = conf.GivePermissionsConf.userEmail()
      val jobId = conf.GivePermissionsConf.jobId()
      val permissionLevel = 
        JobPermissionLevel.valueOf(conf.GivePermissionsConf.permissionLevel())
      println(s"giving ${userEmail} permissions in job ${jobId} of type ${permissionLevel.toString()}")
      mainFunctions
        .givePermissions(userEmail, jobId, permissionLevel, workspaceClient)
    case Some(conf.ListDependentConf) =>
      val databricksConfig = DatabricksConfig()
        .setProfile(conf.ListDependentConf.profile())
      val workspaceClient = WorkspaceClient(databricksConfig)
      val jobId = conf.GivePermissionsConf.jobId()
      mainFunctions.listDependent(jobId, workspaceClient)
    case Some(_) => new Exception("Subcommand not implemented")
    case None => new Exception("Invalid subcommand")

object Main:
    def main(args: Array[String]): Unit = 
        mainWithFunctions(args, MainFunctions())