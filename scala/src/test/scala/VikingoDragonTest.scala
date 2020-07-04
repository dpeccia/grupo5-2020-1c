import org.scalatest.{FreeSpec, Matchers}

class VikingoDragonTest extends FreeSpec with Matchers {
    val hipo: Vikingo = Vikingo(2, 10, SistemaDeVuelo, 10)
    val astrid: Vikingo = Vikingo(15, 20, new Arma(30), 100)
    val furiaNocturna: Dragon = FuriaNocturna(50,100, new Arma(40))
    val chimuelo: Dragon = FuriaNocturna(50, 100, SistemaDeVuelo)
    val gronckle: Dragon = Gronckle(60, 3)

  "Vikingos, dragones y jinetes" - {

    "montura exitosa" - {
      "hipo puede montar a un gronckle porque cumple requisito de peso" in {
        val jinete: Option[Jinete]= hipo.montar(gronckle)
        assert(jinete.isDefined)
      }
      "hipo puede montar a chimuelo porque cumple requisito de peso y cumple requisito particular de este dragon" in {
        val jinete: Option[Jinete]= hipo.montar(chimuelo)
        assert(jinete.isDefined)
      }
    }

    "montura no exitosa" - {
      "patapez no puede montar a un furiaNocturna porque no cumple requisito de peso" in {
        val jinete: Option[Jinete]= patapez.montar(furiaNocturna)
        assert(jinete.isEmpty)
      }
      "astrid no puede montar a chimuelo porque aunque cumple requisito de peso, no cumple requisito particular de este dragon" in {
        val jinete: Option[Jinete]= astrid.montar(chimuelo)
        assert(jinete.isEmpty)
      }
    }

    "mejor montura" - {
      "la mejor montura de Hipo en una carrera es como Jinete con Chimuelo" in {
        val jinete: Competidor = hipo.mejorCompetidor(List(chimuelo), Carrera(None, 10))
        assert(jinete.asInstanceOf[Jinete].dragon.equals(chimuelo))
      }
    }

  }

}
