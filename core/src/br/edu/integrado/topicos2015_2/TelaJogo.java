package br.edu.integrado.topicos2015_2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Misael-Ticiane on 28/09/2015.
 */
public class TelaJogo extends TelaBase {

    private OrthographicCamera camera; // camera do jogo
    private World mundo; // representa o mundo do Box2d
    private Body chao; // corpo do chao
    private Passaro passaro;
    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();
    private Box2DDebugRenderer debug; //desenha o mundo na tela para ajudar o desenvolvimento

    private int pontuaca = 0;
    private BitmapFont fontePontuacao;
    private Stage palcoInformacoes;
    private Label lbPontuacao;
    private ImageButton btnPlay;
    private ImageButton btnGameOver;
    private OrthographicCamera cameraInfo;


    public TelaJogo(MainGame game) {
        super(game);
    }

    @Override
    public void show() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth()/ Util.ESCALA, Gdx.graphics.getHeight() / Util.ESCALA);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0,-9.8f),false); // vetor gravidade (x,y).
        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectarColisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        initPassaro();
        initChao();
        initFontes();
        initInformacoes();
        //new Obstaculo(mundo, camera, null);
    }

    /**
     * verrifica o passaro esta envolvido na colisao
     * @param fixtureA
     * @param fixtureB
     */

    private boolean gamerOver = false;
    private void detectarColisao(Fixture fixtureA, Fixture fixtureB) {
        if("BIRD".equals(fixtureA.getUserData()) || "BIRD".equals(fixtureB.getUserData())){
            gamerOver = true;
        }
    }

    private void initFontes() {
        FreeTypeFontGenerator.FreeTypeFontParameter fonteParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        fonteParam.size = 56;
        fonteParam.color = Color.BLACK;
        fonteParam.shadowColor = Color.WHITE;
        fonteParam.shadowOffsetX = 4;
        fonteParam.shadowOffsetY = 4;

        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));

        fontePontuacao = gerador.generateFont(fonteParam);
        gerador.dispose();
    }

    private void initInformacoes() {
        palcoInformacoes = new Stage(new FillViewport(cameraInfo.viewportWidth, camera.viewportHeight, cameraInfo));

        // seta interaccao com o palco
        Gdx.input.setInputProcessor(palcoInformacoes);

        Label.LabelStyle estilo = new Label.LabelStyle();

        estilo.font = fontePontuacao;

        lbPontuacao = new Label("0",estilo);
        palcoInformacoes.addActor(lbPontuacao);


    }


    private void initChao() {
        chao = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0 ,0);




    }

    private void initPassaro() {
        passaro = new Passaro(mundo, camera, null);

    }

    //delta � o tempo que passo entre o quadro atual e o anterior
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); // limpa a tela e pinta de fundo.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // mantem o buffer de cores.

        capturaTeclas();

        atualizar(delta);


        renderizar(delta);

        debug.render(mundo, camera.combined.cpy().scl(Util.PIXEL_METRO));

    }

    private boolean pulando = false;

    private void capturaTeclas() {
        pulando =false;

        if(Gdx.input.justTouched()){
            pulando = true;
        }
    }

    /**
     * Redenrizar/desenhar as imagens
     *
     *
     * @param delta
     */

    private void renderizar(float delta) {
        palcoInformacoes.draw();

    }

    /**
     * Atualização e cálculo dos corpos
     *
     * @param delta
     */


    private void atualizar(float delta) {
        palcoInformacoes.act(delta);
        passaro.atualizar(delta);

        mundo.step(1f / 60f, 6, 2);

        atualizaInformacoes();
        atualizarObstaculos();
        atualizarChao();
        atualizarCamera();
        if(pulando){
            passaro.pular();
        }
    }

    private void atualizaInformacoes() {
        lbPontuacao.setText(pontuaca + " ");
        lbPontuacao.setPosition(cameraInfo.viewportWidth/2 - lbPontuacao.getPrefWidth() /2,
                cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());
    }

    private void atualizarObstaculos(){
        //
        while(obstaculos.size < 4){
            // ultimo item q foi add a lista?
            Obstaculo ultimo = (obstaculos.size > 0)? obstaculos.peek(): null;

            //cria novo obstaculo
            Obstaculo o = new Obstaculo(mundo,camera,ultimo);

            // add a lista
            obstaculos.add(o);
        }



        // verifica se abstaculo saiu da tela
        for(Obstaculo o: obstaculos){
            // calcula a lateral inicial da camera
            float inicioCamera = passaro.getCorpo().getPosition().x -
                    (camera.viewportWidth/2/ Util.PIXEL_METRO) - o.getLargura();
            // saiu da tela?
            if(inicioCamera > o.getPosX()){
                o.remover();
                obstaculos.removeValue(o,true);
            }else if(!o.isPassou() && o.getPosX() < passaro.getCorpo().getPosition().x){
                o.setPassou(true);
                pontuaca++;
                // reproduzir pontos
            }
        }

    }

    private void atualizarCamera() {
        // converte metro para pixel
        camera.position.x = (passaro.getCorpo().getPosition().x - 32 / Util.PIXEL_METRO ) * Util.PIXEL_METRO;

        camera.update();
    }

    /**
     * Atualiza a posição do chão para companhar o  passaro
     */

    private void atualizarChao() {
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        Vector2 posicao = passaro.getCorpo().getPosition();

        chao.setTransform(posicao.x,0 ,0);


    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA , height / Util.ESCALA);
        camera.update();

        redimensionaChao();

        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
    }

    /**
     * configura o tamanho do chão de acordo com o tamanho da tela;
     */

    private void redimensionaChao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, Util.ALTURA_CHAO / 2);

        Fixture forma = Util.criarForma(chao, shape, "Chao");
        shape.dispose();



    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        debug.dispose();
        mundo.dispose();

        palcoInformacoes.dispose();
        fontePontuacao.dispose();
    }
}
