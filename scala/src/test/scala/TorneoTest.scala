import org.scalatest.{FreeSpec, Matchers}

class TorneoTest extends FreeSpec with Matchers {
  val hipo: Vikingo = Vikingo(2, 25, SistemaDeVuelo, 10)
  val astrid: Vikingo = Vikingo(15, 20, Arma(30), 10)
  val patan: Vikingo = Vikingo(20, 15, Arma(100), 5)

  val chimuelo: Dragon = FuriaNocturna(100, 100, SistemaDeVuelo)
  val nadderMortiferoRojo: Dragon = NadderMortifero(100)
  val gronckle: Dragon = Gronckle(60, 3)

  val festivalDeInvierno: Torneo = Torneo(List(Combate(Some(RequisitoBarbarosidadPosta(13))), Carrera(None, 11), Pesca(None)),
                                          List(chimuelo, nadderMortiferoRojo, gronckle),new Estandar())

  "Participacion Individual" - {
    "patan gana el festival de invierno con regla Estandar" in {
      assertResult(Some(patan.incrementarNivelDeHambre(26))) {
        festivalDeInvierno.realizarTorneoIndividualmente(List(hipo, astrid, patan, Patapez))
      }
    }

    "astrid gana el festival de invierno con regla de Eliminacion" in {
      assertResult(Some(astrid.incrementarNivelDeHambre(21))) {
        festivalDeInvierno.setRegla(Eliminacion(1)).realizarTorneoIndividualmente(List(hipo, astrid, patan, Patapez))
      }
    }

    "Patapez gana el festival de invierno con regla de Torneo Inverso" in {
      assertResult(Some(Patapez.incrementarNivelDeHambre(7.5))) {
        festivalDeInvierno.setRegla(TorneoInverso).realizarTorneoIndividualmente(List(hipo, astrid, patan, Patapez))
      }
    }

    "astrid gana el festival de invierno con regla de Ban de Dragones" in {
      assertResult(Some(astrid.incrementarNivelDeHambre(16))) {
        festivalDeInvierno.setRegla(BanDeDragones(dragon => dragon.peso < 60)).realizarTorneoIndividualmente(List(hipo, astrid, patan, Patapez))
      }
    }

    "patan gana el festival de invierno con Handicap" in {
      assertResult(Some(patan.incrementarNivelDeHambre(16))) {
        festivalDeInvierno.setRegla(Handicap).realizarTorneoIndividualmente(List(hipo, astrid, patan, Patapez))
      }
    }
  }

  "Participacion por Equipos" - {
    val astridYPatapez = Equipo(List(astrid, Patapez))
    val hipoYPatan = Equipo(List(hipo, patan))

    "el equipo de hipoYPatan gana el festival de invierno por equipos con solo Patan en pie" in {
      assertResult(Some(Equipo(List(patan.incrementarNivelDeHambre(16))))) {
        festivalDeInvierno.realizarTorneoPorEquipos(List(astridYPatapez, hipoYPatan))
      }
    }
  }
}
