package fil.iagl.opl.cocospoon.processors;

import java.util.stream.Collectors;

import org.fest.assertions.Assertions;
import org.junit.Test;

import fil.iagl.opl.cocospoon.insert.Insertion;
import fil.iagl.opl.cocospoon.insert.impl.SynchronizedInsert;
import fil.iagl.opl.cocospoon.tools.ContainsSameElementFilter;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.NameFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class SynchronizedInsertTest {

  @Test
  public void instrumentSynchronizedTest() throws Exception {
    Launcher l = new Launcher();

    l.addInputResource("src/test/java/fil/iagl/opl/cocospoon/samples");
    l.buildModel();

    CtClass<?> sample = (CtClass<?>) l.getFactory().Package().getRootPackage().getElements(new NameFilter<>("SynchronizedSample")).get(0);

    Integer nbSynchronizedBlock = 2;
    Integer nbStatementToInsert = 2;
    Insertion insertionStrategy = new SynchronizedInsert();
    CtStatement statementToInsert = l.getFactory().Code().createCodeSnippetStatement("TO BE INSERT");
    Assertions.assertThat(
      sample.getElements(new TypeFilter<CtElement>(CtElement.class))
        .stream().filter(insertionStrategy::match).collect(Collectors.toList()))
      .hasSize(nbSynchronizedBlock);

    sample.getElements(new TypeFilter<CtElement>(CtElement.class))
      .stream().filter(insertionStrategy::match).forEach(element -> insertionStrategy.apply(element, statementToInsert));

    System.out.println(sample);
    Assertions.assertThat(
      sample.getElements(new ContainsSameElementFilter(statementToInsert)))
      .hasSize(nbStatementToInsert);
  }

}
