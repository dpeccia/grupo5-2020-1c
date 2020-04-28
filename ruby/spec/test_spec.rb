require_relative '../lib/trait'
describe Trait do
  before do
    Trait.define do
      name :MiTrait
      method :metodo1 do
        "Hola"
      end
      method :metodo2 do |un_numero|
        un_numero * 0 + 42
      end
    end
      Trait.define do
        name :OtroTrait
        method :metodo3 do
          "Hola"
        end
        method :metodo4 do |un_numero|
          un_numero * 0 + 42
        end
      end
    class A uses MiTrait + MiOtroTrait
    end
  end

  describe '#define crea el trait con nombre' do
    it 'crea trait con nombre y metodos' do
      expect(MiTrait.nombre).to be :MiTrait
    end
  end
  describe 'tiene los metodos' do
    it 'tiene los metodos' do
      expect(MiTrait.methods(false)).to include[:metodo1,:metodo2]
    end
  end
  describe '#+ suma bien los traits'
  it 'la clase tiene todos los metodos' do
    A.methods(false)
  end
end