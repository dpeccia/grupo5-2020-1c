import org.scalatest.{FreeSpec, Matchers}

class PostaTest extends FreeSpec with Matchers{
  val vikingo: Vikingo = Vikingo(10,100,Arma(100),100)
  val vikingoConMasDanio: Vikingo = Vikingo(10,100, Arma(200),100)

  "Requisitos de participacion de una posta" - {

    "Requisito base: que su nivel de hambre no supere el 100%" - {
      "vikingo puede participar de pesca" in {
        assert(Pesca(List(vikingo), None).puedeParticipar(vikingo))
      }
    }

    "Requisitos particulares" - {

      "Requisito Pesca" - {
        "puede no existir un requerimiento para la pesca" in {
          assertCompiles("Pesca(List(vikingo), None)")
        }
        "si existe el requisito debe ser un RequisitoPesoDeterminado" in {
          assertTypeError("Pesca(List(vikingo), Option(new RequisitoMontura))")
          assertCompiles("Pesca(List(vikingo), Option(RequisitoPesoDeterminado(250)))")
        }
        "vikingo puede participar de pesca si cumple requisito" in {
          assert(Pesca(List(vikingo),Option(RequisitoPesoDeterminado(200))).puedeParticipar(vikingo))
        }
        "vikingo no puede participar de pesca si no cumple requisito" in {
          assert(!Pesca(List(vikingo),Option(RequisitoPesoDeterminado(250))).puedeParticipar(vikingo))
        }
      }

      "Requisitos Combate" - {

      }

      "Requisito Carrera" - {
        "puede no existir un requerimiento para la carrera" in {
          assertCompiles("Carrera(List(vikingo), None,10)")
        }
        "si existe el requisito debe ser un RequisitoMontura" in {
          assertTypeError("Carrera(List(vikingo), Option(RequisitoPesoDeterminado(200)),10)")
          assertCompiles("Carrera(List(vikingo), Option(new RequisitoMontura),10)")
        }
        "vikingo no puede participar de carrera porque no es un jinete" in {
          assert(!Carrera(List(vikingo),Option(new RequisitoMontura),10).puedeParticipar(vikingo))
        }
      }
    }

    "Quien es mejor en una posta" - {
      "vikingo que produce mucho danio es mejor que vikingo en un combate" in {
        vikingoConMasDanio.esMejorQue(vikingo)(Combate(List(vikingo),None))
      }
    }

  }
}
