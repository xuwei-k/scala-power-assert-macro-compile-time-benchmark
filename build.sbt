lazy val common = Def.settings(
  scalaVersion := "3.9.0",
)

common

def generateSource(prefix: String) = Def.settings(
  Compile / sourceGenerators += task {
    val dir = (Compile / sourceManaged).value
    (1 to 100).map { n =>
      val f = dir / s"A${n}.scala"
      val src = s"""|package example
                    |
                    |class A${n} {
                    |  private def a: Int = 1
                    |  private def b: Int = 2
                    |
                    |${(1 to 1000).map { m => s"  def f${m}: Unit = _root_.$prefix.assert(a == b)" }.mkString("\n", "\n", "\n")}
                    |}
                    |""".stripMargin
      IO.write(f, src)
      f
    }
  }
)

val p1 = project.settings(
  common,
  generateSource("scala.Predef")
)

val p2 = project.settings(
  common,
  libraryDependencies += "com.lihaoyi" %% "utest" % "0.10.0-RC1",
  generateSource("utest")
)

val p3 = project.settings(
  common,
  libraryDependencies += "com.eed3si9n.verify" %% "verify" % "1.0.0",
  generateSource("verify.asserts.PowerAssert")
)

val p4 = project.settings(
  common,
  libraryDependencies += "org.scalatest" %% "scalatest-core" % "3.2.20",
  generateSource("org.scalatest.Assertions")
)
