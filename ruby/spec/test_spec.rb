require_relative '../lib/trait'

describe Trait do

  before :all do
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
      method :metodo1 do
        "Chau"
      end
      method :metodo4 do |un_numero|
        un_numero * 0 + 42
      end
    end

    class A
      uses MiTrait + OtroTrait
    end

    class B
      uses MiTrait - :metodo2
    end

    class ConAlias
      uses MiTrait << (:metodo1 >> :saludo)
    end
  end

  describe '#define crea el trait con nombre' do
    it 'crea trait con nombre y metodos' do
      expect(MiTrait.nombre).to be :MiTrait
    end
    it 'tiene los metodos' do
      expect(MiTrait.methods(false).sort).to eq [:metodo1,:metodo2]
    end
  end
  describe '#+' do
    it 'la clase tiene todos los metodos' do
      expect(A.new.methods).to include :metodo1,:metodo2,:metodo4
    end
    it 'la clase tira error si tiene metodos repetidos' do
      expect { A.new.metodo1 }.to raise_error("Metodo Repetido")
    end
    end
  describe 'Tests de Resta' do
    it 'Se remueve el metodo especificado' do
      expect(B.new.methods).to include :metodo1
    end
  end

    describe '#<< metodo renombrar' do
      it 'clase deberia tener los metodos viejos y el renombrado' do
        expect(ConAlias.new).to respond_to :metodo1 and expect(ConAlias.new).to respond_to :saludo
      end
    end
  end
  #Test que devuelve error si se saca un metodo que no esta