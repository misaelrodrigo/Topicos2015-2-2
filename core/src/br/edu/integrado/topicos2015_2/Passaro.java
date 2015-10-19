package br.edu.integrado.topicos2015_2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Misael-Ticiane on 19/10/2015.
 */
public class Passaro {

    private final World mundo;
    private final OrthographicCamera camera;
    private final Texture[] texturas;


    private Body corpo;

    public Passaro(World mundo , OrthographicCamera camera, Texture[] texturas){

        this.mundo = mundo;
        this.camera = camera;
        this.texturas = texturas;
        initCorpo();
    }

    private void initCorpo(){
        float x = (camera.viewportWidth / 2) / Util.PIXEL_METRO;
        float y = (camera.viewportHeight / 2) / Util.PIXEL_METRO;

        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, x, y);

        FixtureDef def = new FixtureDef();
        def.density = 1;
        def.friction = 0.4f;
        def.restitution = 0.3f;
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("physics/bird.json"));
        loader.attachFixture(corpo, "bird", def, 1, "bird");
    }

    public void pular(){
        corpo.setLinearVelocity(corpo.getLinearVelocity().x,0);
        corpo.applyForceToCenter(0, 100, false);
    }

    /**
     * atualiza o comportamento do passaro
     * @param delta
     */
    public void atualizar(float delta){
        atualizaVelocidade();

    }

    private void atualizaVelocidade() {
        corpo.setLinearVelocity(2f, corpo.getLinearVelocity().y);

    }


    public Body getCorpo() {
        return corpo;
    }
}