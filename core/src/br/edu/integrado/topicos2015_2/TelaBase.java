package br.edu.integrado.topicos2015_2;

import com.badlogic.gdx.Screen;

/**
 * Created by Misael-Ticiane on 28/09/2015.
 * Tela abstrata que implementa a Screen
 */
public abstract class TelaBase implements Screen{

    // quase private, mais pode ser acessado pela herança
    protected MainGame game;
    public TelaBase(MainGame game) {
        this.game = game;
    }

    @Override
    public void hide() {
        dispose();
    }
}
