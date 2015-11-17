package br.edu.integrado.topicos2015_2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
    private Texture[] texturasPassaro;
    private Texture texturaObstaculoCima;
    private Texture texturaObstaculoBaixo;
    private Texture texturaChao;
    private Texture texturaFundo;
    private Texture texturaPlay;
    private Texture texturaGameOver;

private boolean jogoIniciado = false;

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
        initTexturas();
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

    private void initTexturas() {
        texturasPassaro = new Texture[3];
        texturasPassaro[0] = new Texture("sprites/bird-1.png");
        texturasPassaro[1] = new Texture("sprites/bird-2.png");
        texturasPassaro[2] = new Texture("sprites/bird-3.png");

            texturaObstaculoCima = new Texture("sprites/toptube.png");
        texturaObstaculoBaixo = new Texture("sprites/bottomtube.png");
        texturaFundo = new Texture("sprites/bg.png");
        texturaChao = new Texture("sprites/ground.png");

            texturaPlay = new Texture("sprites/playbtn.png");
        texturaGameOver = new Texture("sprites/gameover.png");
    }

    private boolean gameOver = false;

    private void initInformacoes() {
        palcoInformacoes = new Stage(new FillViewport(cameraInfo.viewportWidth, camera.viewportHeight, cameraInfo));

        // seta interaccao com o palco
        Gdx.input.setInputProcessor(palcoInformacoes);

        Label.LabelStyle estilo = new Label.LabelStyle();

        estilo.font = fontePontuacao;

        lbPontuacao = new Label("0", estilo);
        palcoInformacoes.addActor(lbPontuacao);


        //inicia botões
        ImageButton.ImageButtonStyle estiloBotao =
                new ImageButton.ImageButtonStyle();

        estiloBotao.up = new SpriteDrawable(new Sprite(texturaPlay));
        btnPlay = new ImageButton(estiloBotao);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                jogoIniciado = true;
            }
        });
        palcoInformacoes.addActor(btnPlay);

        estiloBotao = new ImageButton.ImageButtonStyle();
        estiloBotao.up = new SpriteDrawable(new Sprite(texturaGameOver));

        btnGameOver = new ImageButton(estiloBotao);
        btnGameOver.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                reiniciarJogo();
            }
        });
        palcoInformacoes.addActor(btnGameOver);
    }


            /**
     * Recria a tela do jogo com todos os seus componentes
      */

            private void reiniciarJogo() {
                //aqui vai o código de reiniciar;
                game.setScreen(new TelaJogo(game));


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
        passaro.getCorpo().setFixedRotation(!gameOver);
        passaro.atualizar(delta, !gameOver);

        if(jogoIniciado) {
            mundo.step(1f / 60f, 6, 2);
            atualizarObstaculos();
        }

        atualizaInformacoes();

        if(!gameOver){
            atualizarCamera();
            atualizarChao();

        }

        if (pulando && !gameOver && jogoIniciado){
            passaro.pular();
        }
    }

    private void atualizaInformacoes() {
        lbPontuacao.setText(pontuaca + " ");
        lbPontuacao.setPosition(cameraInfo.viewportWidth/2 - lbPontuacao.getPrefWidth() /2,
                cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());
        btnPlay.setPosition(
                cameraInfo.viewportWidth / 2 - btnPlay.getPrefWidth() / 2,
                cameraInfo.viewportHeight / 2 - btnPlay.getPrefHeight() * 2
               );
        btnPlay.setVisible(!jogoIniciado);

        btnGameOver.setPosition(
        cameraInfo.viewportWidth / 2 - btnGameOver.getPrefWidth() / 2,
                        cameraInfo.viewportHeight / 2 - btnGameOver.getPrefHeight() / 2
                        );
        btnGameOver.setVisible(gameOver);
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
        texturasPassaro[0].dispose();
        texturasPassaro[1].dispose();
        texturasPassaro[2].dispose();

        texturaObstaculoCima.dispose();
        texturaObstaculoBaixo.dispose();

        texturaFundo.dispose();
        texturaChao.dispose();

        texturaPlay.dispose();
        texturaGameOver.dispose();
    }
}
