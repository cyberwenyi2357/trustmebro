package scm.yiwen4.molecue_go;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ArFragment fragment;
    private ModelRenderable modelRenderable;
    private int[] models = {R.raw.connector,R.raw.hydrogen,R.raw.oxygen};
    private Random rand = new Random();
    private ModelRenderable[] renderables = new ModelRenderable[models.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        ModelRenderable.builder()
                .setSource(this, R.raw.hydrogen)
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e("HelloARCore", "Unable to load Renderable.", throwable);
                            return null;
                        });
        for (int index = 0; index < renderables.length; index++) {
            int finalIndex = index;
            ModelRenderable.builder()
                    .setSource(this, models[finalIndex]) // exercise 1
                    .setIsFilamentGltf(true)
                    .build()
                    .thenAccept(renderable -> renderables[finalIndex] = renderable)
                    .exceptionally(
                            throwable -> {
                                Log.e("HelloARCore", "Unable to load Renderable.", throwable);
                                return null;
                            });
        }
///sssssssss
        fragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    int renderable_index = rand.nextInt(renderables.length); // exercise 1
                    if (renderables[renderable_index] == null) {
                        return;
                    }
                    // Create an anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(fragment.getArSceneView().getScene());
                    TransformableNode modelNode = new TransformableNode(fragment.getTransformationSystem());
                    modelNode.setParent(anchorNode);
                    modelNode.setRenderable(modelRenderable);
                    modelNode.select(); // select the newly added object
//sss
                });


    }
}