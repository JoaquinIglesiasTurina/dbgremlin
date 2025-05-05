import org.scalamock.stubs.Stubs
import com.databricks.sdk.service.jobs.JobPermissionLevel

class MainSuite extends munit.FunSuite, Stubs:
  test("call main function to give default permissions"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions.returns((
      userEmail: String, 
      jobId: Long,
      permissionLevel: JobPermissionLevel) => ())
    mainWithFunctions(
      Array("give-job-permissions", "--job-id", "1234", "--user-email", "some@email.com"), 
      mainFunctions)
    assertEquals(mainFunctions.givePermissions.times, 1)
    assertEquals(
      mainFunctions.givePermissions.calls,
      List(("some@email.com", 1234L, JobPermissionLevel.CAN_MANAGE_RUN)))

  test("options work as expected"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions.returns((
      userEmail: String, 
      jobId: Long,
      permissionLevel: JobPermissionLevel) => ())
    mainWithFunctions(
      Array("give-job-permissions", 
      "--job-id", "1234", 
      "--user-email", "some@email.com", "--permission-level", "CAN_VIEW"), 
      mainFunctions)
    assertEquals(
      mainFunctions.givePermissions.calls,
      List(("some@email.com", 1234L, JobPermissionLevel.CAN_VIEW)))