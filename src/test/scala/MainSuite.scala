import org.scalamock.stubs.Stubs

class MainSuite extends munit.FunSuite, Stubs:
  test("call main function to give permissions"):
    val mainFunctions = stub[MainFunctions]
    mainFunctions.givePermissions
      .returns((userEmail: String, jobId: Long) => ())
    mainWithFunctions(
        Array("give-job-permissions", "--job-id", "1234", "--user-email", "some@email.com"), 
        mainFunctions)
    assertEquals(mainFunctions.givePermissions.times, 1)
    assertEquals(mainFunctions.givePermissions.calls, List(("some@email.com", 1234L)))