import org.scalatest.{FreeSpec, Matchers}

class CompetidoresTest extends FreeSpec with Matchers{
    val vikingo: Vikingo = new Vikingo(10,100,new Arma(100),100)
    val furiaNocturna: Dragon = FuriaNocturna(100,100)
    val nadderMortifero: Dragon = NadderMortifero(50)
  "Vikingos, dragones y jinetes" - {

    "montar dragon" - {
      "vikingo puede montar a furiaNocturna porque cumple requisito de peso" in {
        val jinete: Option[Jinete]= vikingo.montar(furiaNocturna)
        assert(jinete.get.isInstanceOf[Jinete])
      }
      "Vikingo no puede montar nadderMortifero devuelve None" in {
        val jinete: Option[Jinete]= vikingo.montar(nadderMortifero)
        assert(jinete.isEmpty)
      }
    }
  }
}
