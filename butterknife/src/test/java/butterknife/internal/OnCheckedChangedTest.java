package butterknife.internal;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static butterknife.internal.ProcessorTestUtilities.butterknifeProcessors;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class OnCheckedChangedTest {
  @Test public void checkedChanged() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import butterknife.OnCheckedChanged;",
        "public class Test extends Activity {",
        "  @OnCheckedChanged(1) void doStuff() {}",
        "}"
    ));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findRequiredView(source, 1, \"method 'doStuff'\");",
            "    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {",
            "      @Override public void onCheckedChanged(android.widget.CompoundButton p0, boolean p1) {",
            "        target.doStuff();",
            "      }",
            "    });",
            "  }",
            "  public static void reset(test.Test target) {",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}
