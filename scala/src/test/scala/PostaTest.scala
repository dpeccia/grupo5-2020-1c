import org.scalatest.{FreeSpec, Matchers}

class PostaTest extends FreeSpec with Matchers{
  val vikingo: Vikingo = Vikingo(10,100,Arma(100),100)
  val vikingoConMasDanio: Vikingo = Vikingo(10,100, Arma(200),100)

  "Requisitos de participacion de una posta" - {

    "Requisito base: que su nivel de hambre no supere el 100%" - {
      "vikingo puede participar de pesca" in {
        assert(Pesca(None).puedeParticipar(vikingo))
      }
    }

    "Requisitos particulares" - {

      "Requisito Pesca" - {
        "puede no existir un requerimiento para la pesca" in {
          assertCompiles("Pesca(None)")
        }
        "si existe el requisito debe ser un RequisitoPesoDeterminado" in {
          assertTypeError("Pesca(Option(new RequisitoMontura))")
          assertCompiles("Pesca(Option(RequisitoPesoDeterminado(250)))")
        }
        "vikingo puede participar de pesca si cumple requisito" in {
          assert(Pesca(Option(RequisitoPesoDeterminado(200))).puedeParticipar(vikingo))
        }
        "vikingo no puede participar de pesca si no cumple requisito" in {
          assert(!Pesca(Option(RequisitoPesoDeterminado(250))).puedeParticipar(vikingo))
        }
      }

      "Requisitos Combate" - {

      }

      "Requisito Carrera" - {
        "puede no existir un requerimiento para la carrera" in {
          assertCompiles("Carrera(None,10)")
        }
        "si existe el requisito debe ser un RequisitoMontura" in {
          assertTypeError("Carrera(Option(RequisitoPesoDeterminado(200)),10)")
          assertCompiles("Carrera(Option(new RequisitoMontura),10)")
        }
        "vikingo no puede participar de carrera porque no es un jinete" in {
          assert(!Carrera(Option(new RequisitoMontura),10).puedeParticipar(vikingo))
        }
      }
    }

    "Quien es mejor en una posta" - {
      "vikingo que produce mucho danio es mejor que vikingo en un combate" in {
        vikingoConMasDanio.esMejorQue(vikingo)(Combate(None))
      }
    }

  }
}
