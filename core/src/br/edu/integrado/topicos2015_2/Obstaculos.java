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
public class Obstaculos {
    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posX;
    private float posY;
    private float posYBaixo;
    private float posYCima;
    private float largura, altura;
    private boolean passou;
    private Obstaculos ultimoObjtaculo; // ultimo antes do atual.

    public Obstaculos(World mundo, OrthographicCamera camera, Obstaculos ultimoObjtaculo) {
        this.mundo = mundo;
        this.camera = camera;
        this.ultimoObjtaculo = ultimoObjtaculo;

        initPosicao();
        initCorpoCima();
        initCorpoBaixo();

    }




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

        float xInicial = largura;
        if(ultimoObjtaculo != null){
            xInicial = ultimoObjtaculo.getPosX();
        }
        xInicial = xInicial + 8; // adiciona o espaco entre os obstaculos

        // tamanho da tela divido por 6, para encontrar posicao y do obstaculo
        float parcela = (altura - Util.ALTURA_CHAO) / 6;

        float multiplicador = MathUtils.random(1,3);

        posYBaixo = Util.ALTURA_CHAO + (parcela + multiplicador);

        posYCima = posYBaixo + altura +2f;

    }

    public float getPosX() {
        return posX;
    }

    public void remover(){
        mundo.destroyBody(corpoCima);
        mundo.destroyBody(corpoBaixo);
    }
}
