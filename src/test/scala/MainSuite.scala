import org.scalamock.stubs.Stubs
import com.databricks.sdk.service.jobs.JobPermissionLevel
import com.databricks.sdk.WorkspaceClient

class MainSuite extends munit.FunSuite, Stubs:
  val workspaceClient = new WorkspaceClient()
  test("call main function to give default permissions"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions.returns((
      userEmail: String, 
      jobId: Long,
      permissionLevel: JobPermissionLevel,
      workspaceClient: WorkspaceClient) => ())
    mainWithFunctions(
      Array("give-job-permissions", "--job-id", "1234", "--user-email", "some@email.com"), 
      mainFunctions)
    assertEquals(mainFunctions.givePermissions.times, 1)
    assertEquals(
      mainFunctions.givePermissions.calls.head.take(3),
      ("some@email.com", 1234L, JobPermissionLevel.CAN_MANAGE))

  test("options work as expected"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions.returns((
      userEmail: String, 
      jobId: Long,
      permissionLevel: JobPermissionLevel,
      workspaceClient: WorkspaceClient) => ())
    mainWithFunctions(
      Array("give-job-permissions", 
      "--job-id", "1234", 
      "--user-email", "some@email.com", "--permission-level", "CAN_VIEW"), 
      mainFunctions)
    assertEquals(
      mainFunctions.givePermissions.calls.head.take(3),
      ("some@email.com", 1234L, JobPermissionLevel.CAN_VIEW))

  test("can pass databricks profile"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions.returns((
      userEmail: String, 
      jobId: Long,
      permissionLevel: JobPermissionLevel,
      workspaceClient: WorkspaceClient
    ) => ())
    mainWithFunctions(
      Array("give-job-permissions",
      "--profile", "DEFAULT",
      "--job-id", "1234", 
      "--user-email", "some@email.com", "--permission-level", "CAN_VIEW"), 
      mainFunctions)

  test("call list dependent"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.listDependent
      .returns((jobId: Long, workspaceClient: WorkspaceClient) => ())
    mainWithFunctions(
        Array("list-dependent", "--job-id", "1234"), 
        mainFunctions)
    assertEquals(mainFunctions.listDependent.times, 1)
    assertEquals(mainFunctions.listDependent.calls.head.take(1), Tuple(1234L))