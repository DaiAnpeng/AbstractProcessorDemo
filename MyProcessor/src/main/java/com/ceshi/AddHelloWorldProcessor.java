package com.ceshi;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.ceshi.AddHelloWorld")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class AddHelloWorldProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        final JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
        final TreeMaker treeMaker = TreeMaker.instance(context);
        Set<? extends Element> elements = roundEnv.getRootElements();

        for (Element element : roundEnv.getElementsAnnotatedWith(AddHelloWorld.class)) {
            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) elementUtils.getTree(element);

            treeMaker.pos = jcMethodDecl.pos;
            jcMethodDecl.body = treeMaker.Block(0, List.of(
                    treeMaker.Exec(
                            treeMaker.Apply(
                                    List.<JCTree.JCExpression>nil(),
                                    treeMaker.Select(
                                            treeMaker.Select(
                                                    treeMaker.Ident(
                                                            elementUtils.getName("System")
                                                    ),
                                                    elementUtils.getName("out")
                                            ),
                                            elementUtils.getName("println")
                                    ),
                                    List.<JCTree.JCExpression>of(
                                            treeMaker.Literal("Hello, world!!!")
                                    )
                            )
                    ),
                    jcMethodDecl.body
            ));
        }
        return false;
    }
}
