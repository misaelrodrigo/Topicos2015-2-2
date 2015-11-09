package br.edu.integrado.topicos2015_2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Misael-Ticiane on 26/10/2015.
 */
public class Obstaculo {
    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posX;
    private float posY;
    private float posYBaixo;
    private float posYCima;
    private float largura, altura;
    private boolean passou;
    private Obstaculo ultimoObstaculo; // ultimo antes do atual.

    public Obstaculo(World mundo, OrthographicCamera camera, Obstaculo ultimoObstaculo) {
        this.mundo = mundo;
        this.camera = camera;
        this.ultimoObstaculo = ultimoObstaculo;

        initPosicao();
        initCorpoCima();
        initCorpoBaixo();

    }



/*
    private void initCorpoBaixo() {
        corpoBaixo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYBaixo);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);

        Util.criarForma(corpoCima, shape, "OBSTACULO_BAIXO");
        shape.dispose();


    }

    private void initCorpoCima() {
        corpoCima = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYCima);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura /2);

        Util.criarForma(corpoCima, shape, "OBSTACULO_CIMA");
        shape.dispose();

    }

    private void initPosicao() {
        largura = 40 / Util.PIXEL_METRO;
        altura = camera.viewportHeight / Util.PIXEL_METRO;

        float xInicial = largura + (camera.viewportWidth / 2 / Util.PIXEL_METRO);
        if(ultimoObjtaculo != null){
            xInicial = ultimoObjtaculo.getPosX();
        }
        xInicial = xInicial + 4; // adiciona o espaco entre os obstaculos

        // tamanho da tela divido por 6, para encontrar posicao y do obstaculo
        float parcela = (altura - Util.ALTURA_CHAO) / 6;

        int multiplicador = MathUtils.random(1,3);

        posYBaixo = Util.ALTURA_CHAO + (parcela + multiplicador) - (altura / 2);

        posYCima = posYBaixo + altura +2f;

    }
*/


    private void initPosicao() {
        largura = 40 / Util.PIXEL_METRO;
        altura = camera.viewportHeight / Util.PIXEL_METRO;

        float xInicial = largura;
        // se ja tem outro obstaculo na tela.
        if (ultimoObstaculo != null) {
            xInicial = ultimoObstaculo.getPosX();
        }

        posX = xInicial + 4; // O 4 � o espa�o entre os obstaculos.

        // Tamanho da tela dividida para encontrar a posi��o Y do obstaculo.
        float parcela = (altura - Util.ALTURA_CHAO) / 6;

        int multiplicador = MathUtils.random(1, 3); // escolhe onde o obstaculo ira ser criado.

        posYBaixo = Util.ALTURA_CHAO + (parcela * multiplicador) - (altura / 2);
        posYCima = posYBaixo + altura + 2f; // 2f � o espa�o entre os onstaculos de cima e de baixo.

    }



    private void initCorpoCima() {
        corpoCima = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYCima);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);

        Util.criarForma(corpoCima, shape, "OBSTACULO_CIMA");

        shape.dispose();

    }



    private void initCorpoBaixo() {
        corpoBaixo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYBaixo);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);

        Util.criarForma(corpoBaixo, shape, "OBSTACULO_BAIXO");

        shape.dispose();

    }
    public float getPosX() {
        return posX;
    }

    public void remover(){
        mundo.destroyBody(corpoCima);
        mundo.destroyBody(corpoBaixo);
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public boolean isPassou() {
        return passou;
    }

    public void setPassou(boolean passou) {
        this.passou = passou;
    }
}
