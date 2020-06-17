import com.sun.net.httpserver.Authenticator.Success

import scala.util.Success

trait Competidor {
  def puedoParticipar(posta: Posta): Boolean
  def tieneArma: Boolean
  def cuantoPuedeCargar: Double
  def danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor
  def barbarosidad: Double
  def nivelDeHambre: Double
  def esMejorQue(otroCompetidor: Competidor)(posta: Posta): Boolean = posta.mejorCompetidor(List(this,otroCompetidor)) != otroCompetidor
}

case class Vikingo(var peso: Double,var barbarosidad: Double,var item: Item,var velocidad: Double) extends Competidor {

  var nivelDeHambre: Double = 0

  def tieneArma: Boolean = item.isInstanceOf [Arma]

  def tieneItem(unItem: Item): Boolean = item.equals(unItem)

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedoParticipar(posta: Posta): Boolean = {
    var unCompetidor: Competidor = incrementarNivelDeHambre(posta.nivelDeHambreQueIncrementa)
    unCompetidor.nivelDeHambre < 100
  }

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  //def dragonesQuePuedeMontar(dragones: List[Dragon]): List[Option[Jinete]] = dragones.map(dragon => montar(dragon))

  //def mejorDragonParaPosta(dragones: List[Dragon],posta: Posta): Competidor = posta.ordenarSegunCondicion(dragonesQuePuedeMontar(dragones))

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor = copy(nivelDeHambre = nivelDeHambre + hambreAIncrementar) //TODO preguntar si tiene efecto o no, por lo que dice el requerimiento

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(Jinete(this, dragon))
    }
    None
  }

}

case class Jinete(var vikingo: Vikingo, var dragon: Dragon) extends Competidor {
  def nivelDeHambre: Double = vikingo.nivelDeHambre

  def puedoParticipar(posta: Posta): Boolean = vikingo.puedoParticipar(posta)

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05)

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma
}

case class Patapez(_peso: Double,_barbarosidad: Double, _velocidad: Double,_item: Item = Comestible(10)) extends Vikingo(_peso,_barbarosidad,_item,_velocidad){
  //TODO preguntar la cantidad de calorias del comestible, por ahora le puse 10 para ponerle un numero
  override def puedoParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor = super.incrementarNivelDeHambre(hambreAIncrementar*2)
}
