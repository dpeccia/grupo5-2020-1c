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

class Vikingo(_peso: Double, _barbarosidad: Double, _item: Item, _velocidad: Double) extends Competidor {
  var peso: Double = _peso
  var item: Item = _item
  //var nivelDeHambre: Double = 0
  var barbarosidad: Double = _barbarosidad
  var velocidad: Double = _velocidad

  def nivelDeHambre: Double = 0

  def tieneArma: Boolean = item.isInstanceOf [Arma]

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedoParticipar(posta: Posta): Boolean = (nivelDeHambre + posta.nivelDeHambreQueIncrementa) < 100

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def incrementarNivelDeHambre(hambreAIncrementar: Double) = nivelDeHambre += hambreAIncrementar

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(new Jinete(this, dragon))
    }
    return None
  }

}

class Jinete(_vikingo: Vikingo, _dragon: Dragon) extends Competidor {
  val vikingo = _vikingo
  val dragon = _dragon

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double) = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05)

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma
}

class Patapez(_peso: Double, val _barbarosidad: Double, val _item: Item, val _velocidad: Double) extends Vikingo(_peso,_barbarosidad,_item,_velocidad)
  override def nivelDeHambre:Double = 1

  override def puedoParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = super.incrementarNivelDeHambre(hambreAIncrementar*2)
}
