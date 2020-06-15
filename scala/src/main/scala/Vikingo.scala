import com.sun.net.httpserver.Authenticator.Success

import scala.util.Success

trait Competidor {
  def tieneArma: Boolean
  def cuantoPuedeCargar: Double
  def danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double)
  def barbarosidad: Double
}

class Vikingo(var peso: Double,var barbarosidad: Double,var item: Item,var velocidad: Double) extends Competidor {
  var nivelDeHambre: Double = 0

  def tieneArma: Boolean = item.isInstanceOf [Arma]

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedoParticipar(posta: Posta): Boolean = (nivelDeHambre + posta.nivelDeHambreQueIncrementa) < 100

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = nivelDeHambre += hambreAIncrementar

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(new Jinete(this, dragon))
    }
    None
  }

}

class Jinete(var vikingo: Vikingo, var dragon: Dragon) extends Competidor {

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05)

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma
}

class Patapez(_peso: Double,_barbarosidad: Double, _velocidad: Double,_item: Item = new Comestible(10)) extends Vikingo(_peso,_barbarosidad,_item,_velocidad){
  //TODO preguntar la cantidad de calorias del comestible, por ahora le puse 10 para ponerle un numero
  override def puedoParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = super.incrementarNivelDeHambre(hambreAIncrementar*2)

}
