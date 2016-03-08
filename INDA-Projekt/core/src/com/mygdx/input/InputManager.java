package com.mygdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.core.GlobalVar;
import com.mygdx.player.Player;
import com.mygdx.player.Player.State;
import com.mygdx.utils.ItemPlacer;

public class InputManager {

	public static void handleGlobalInput() {
		if (Gdx.input.isKeyJustPressed(InputConfig.QUIT))
			Gdx.app.exit();
	}

	public static void handleInput(Player player) {
		if (player.dead())
			return;

		if (Gdx.input.isKeyJustPressed(player.keyBomb()))
			ItemPlacer.dropBomb(player);

		if (Gdx.input.isKeyPressed(player.keyUp())) {
			player.setState(State.Up);

			if (Math.abs(player.body.getLinearVelocity().y) < GlobalVar.MAX_MOVE_SPEED)
				player.body.applyForceToCenter(new Vector2(0, GlobalVar.MOVE_FORCE), true);

			return;
		}

		if (Gdx.input.isKeyPressed(player.keyDown())) {
			player.setState(State.Down);

			if (Math.abs(player.body.getLinearVelocity().y) < GlobalVar.MAX_MOVE_SPEED)
				player.body.applyForceToCenter(new Vector2(0, -GlobalVar.MOVE_FORCE), true);

			return;
		}

		if (Gdx.input.isKeyPressed(player.keyLeft())) {
			player.setState(State.Left);

			if (Math.abs(player.body.getLinearVelocity().x) < GlobalVar.MAX_MOVE_SPEED)
				player.body.applyForceToCenter(new Vector2(-GlobalVar.MOVE_FORCE, 0), true);

			return;
		}

		if (Gdx.input.isKeyPressed(player.keyRight())) {
			player.setState(State.Right);

			if (Math.abs(player.body.getLinearVelocity().x) < GlobalVar.MAX_MOVE_SPEED)
				player.body.applyForceToCenter(new Vector2(GlobalVar.MOVE_FORCE, 0), true);

			return;
		}
	}

}
