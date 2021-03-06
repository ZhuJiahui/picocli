/*
   Copyright 2017 Remko Popma

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package picocli;

import org.junit.Test;
import picocli.CommandLine.*;

import java.io.File;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;
import static picocli.HelpTestUtil.usageString;

public class CommandLineMixinTest {

    @Test
    public void testMixinAnnotationIsSufficientToBeRecognizedAsCommand() {
        @Command class MixMeIn {}

        // note: the only annotation in this class is @Mixin
        class Receiver {
            @Mixin MixMeIn mixMeIn;
        }
    }

    static class CommandAttributes {
        @Command(name = "mixmein",
                version = "Mixin 1.0",
                separator = ":",
                description = "description from mixin",
                descriptionHeading = "Mixin Description Heading%n",
                header = "Mixin Header",
                headerHeading = "Mixin Header Heading%n",
                footer = "Mixin Footer",
                footerHeading = "Mixin Footer Heading%n",
                optionListHeading = "Mixin Option List Heading%n",
                parameterListHeading = "Mixin Parameter List Heading%n",
                commandListHeading = "Mixin Command List Heading%n",
                requiredOptionMarker = '%',
                synopsisHeading = "Mixin Synopsis Heading%n",
                abbreviateSynopsis = true,
                customSynopsis = "Mixin custom synopsis",
                showDefaultValues = true,
                sortOptions = false)
        static class MixMeIn {}
    }

    @Test
    public void testMixinAnnotationCommandAttributes() throws Exception {
        class Receiver {
            @Mixin
            CommandAttributes.MixMeIn mixMeIn;
        }

        CommandLine commandLine = new CommandLine(new Receiver());
        verifyMixinCommandAttributes(commandLine);
    }

    @Test
    public void testAddMixinCommandAttributes() throws Exception {
        @Command class Receiver {}

        CommandLine commandLine = new CommandLine(new Receiver());
        commandLine.addMixin("mixmein", new CommandAttributes.MixMeIn());
        verifyMixinCommandAttributes(commandLine);
    }

    private void verifyMixinCommandAttributes(CommandLine commandLine) throws UnsupportedEncodingException {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals("mixmein", commandSpec.name());
        assertArrayEquals(new String[] {"Mixin 1.0"}, commandSpec.version());
        assertEquals(":", commandSpec.separator());
        assertArrayEquals(new String[] {"description from mixin"}, commandSpec.description());
        assertEquals("Mixin Description Heading%n", commandSpec.descriptionHeading());
        assertArrayEquals(new String[] {"Mixin Header"}, commandSpec.header());
        assertEquals("Mixin Header Heading%n", commandSpec.headerHeading());
        assertArrayEquals(new String[] {"Mixin Footer"}, commandSpec.footer());
        assertEquals("Mixin Footer Heading%n", commandSpec.footerHeading());
        assertEquals("Mixin Option List Heading%n", commandSpec.optionListHeading());
        assertEquals("Mixin Parameter List Heading%n", commandSpec.parameterListHeading());
        assertEquals("Mixin Command List Heading%n", commandSpec.commandListHeading());
        assertEquals('%', commandSpec.requiredOptionMarker());
        assertEquals("Mixin Synopsis Heading%n", commandSpec.synopsisHeading());
        assertTrue("abbreviateSynopsis", commandSpec.abbreviateSynopsis());
        assertArrayEquals(new String[] {"Mixin custom synopsis"}, commandSpec.customSynopsis());
        assertTrue("showDefaultValues", commandSpec.showDefaultValues());
        assertFalse("sortOptions", commandSpec.sortOptions());

        String expected = String.format("" +
                "Mixin Header Heading%n" +
                "Mixin Header%n" +
                "Mixin Synopsis Heading%n" +
                "Mixin custom synopsis%n" +
                "Mixin Description Heading%n" +
                "description from mixin%n" +
                "Mixin Footer Heading%n" +
                "Mixin Footer%n");
        assertEquals(expected, usageString(commandLine, Help.Ansi.OFF));
    }

    static class CommandAttributesDontOverwriteReceiverAttributes {
        @Command(name = "mixmein",
                version = "Mixin 1.0",
                separator = ":",
                description = "description from mixin",
                descriptionHeading = "Mixin Description Heading%n",
                header = "Mixin Header",
                headerHeading = "Mixin Header Heading%n",
                footer = "Mixin Footer",
                footerHeading = "Mixin Footer Heading%n",
                optionListHeading = "Mixin Option List Heading%n",
                parameterListHeading = "Mixin Parameter List Heading%n",
                commandListHeading = "Mixin Command List Heading%n",
                requiredOptionMarker = '%',
                synopsisHeading = "Mixin Synopsis Heading%n",
                abbreviateSynopsis = true,
                customSynopsis = "Mixin custom synopsis",
                showDefaultValues = true,
                sortOptions = false)
        static class MixMeIn {}
    }

    @Test
    public void testMixinAnnotationCommandAttributesDontOverwriteReceiverAttributes() throws Exception {

        @Command(name = "receiver",
                version = "Receiver 1.0",
                separator = "~",
                description = "Receiver description",
                descriptionHeading = "Receiver Description Heading%n",
                header = "Receiver Header",
                headerHeading = "Receiver Header Heading%n",
                footer = "Receiver Footer",
                footerHeading = "Receiver Footer Heading%n",
                optionListHeading = "Receiver Option List Heading%n",
                parameterListHeading = "Receiver Parameter List Heading%n",
                commandListHeading = "Receiver Command List Heading%n",
                requiredOptionMarker = '#',
                synopsisHeading = "Receiver Synopsis Heading%n",
                customSynopsis = "Receiver custom synopsis")
        class Receiver {
            @Mixin
            CommandAttributesDontOverwriteReceiverAttributes.MixMeIn mixMeIn;
        }

        CommandLine commandLine = new CommandLine(new Receiver());
        verifyMixinCommandAttributesDontOverwriteReceiverAttributes(commandLine);
    }

    @Test
    public void testAddMixinCommandAttributesDontOverwriteReceiverAttributes() throws Exception {
        @Command(name = "receiver",
                version = "Receiver 1.0",
                separator = "~",
                description = "Receiver description",
                descriptionHeading = "Receiver Description Heading%n",
                header = "Receiver Header",
                headerHeading = "Receiver Header Heading%n",
                footer = "Receiver Footer",
                footerHeading = "Receiver Footer Heading%n",
                optionListHeading = "Receiver Option List Heading%n",
                parameterListHeading = "Receiver Parameter List Heading%n",
                commandListHeading = "Receiver Command List Heading%n",
                requiredOptionMarker = '#',
                synopsisHeading = "Receiver Synopsis Heading%n",
                customSynopsis = "Receiver custom synopsis")
        class Receiver {}

        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        commandLine.addMixin("mixMeIn", new CommandAttributesDontOverwriteReceiverAttributes.MixMeIn());
        verifyMixinCommandAttributesDontOverwriteReceiverAttributes(commandLine);
    }

    private void verifyMixinCommandAttributesDontOverwriteReceiverAttributes(CommandLine commandLine) throws UnsupportedEncodingException {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals("receiver", commandSpec.name());
        assertArrayEquals(new String[] {"Receiver 1.0"}, commandSpec.version());
        assertEquals("~", commandSpec.separator());
        assertArrayEquals(new String[] {"Receiver description"}, commandSpec.description());
        assertEquals("Receiver Description Heading%n", commandSpec.descriptionHeading());
        assertArrayEquals(new String[] {"Receiver Header"}, commandSpec.header());
        assertEquals("Receiver Header Heading%n", commandSpec.headerHeading());
        assertArrayEquals(new String[] {"Receiver Footer"}, commandSpec.footer());
        assertEquals("Receiver Footer Heading%n", commandSpec.footerHeading());
        assertEquals("Receiver Option List Heading%n", commandSpec.optionListHeading());
        assertEquals("Receiver Parameter List Heading%n", commandSpec.parameterListHeading());
        assertEquals("Receiver Command List Heading%n", commandSpec.commandListHeading());
        assertEquals('#', commandSpec.requiredOptionMarker());
        assertEquals("Receiver Synopsis Heading%n", commandSpec.synopsisHeading());
        assertArrayEquals(new String[] {"Receiver custom synopsis"}, commandSpec.customSynopsis());

        String expected = String.format("" +
                "Receiver Header Heading%n" +
                "Receiver Header%n" +
                "Receiver Synopsis Heading%n" +
                "Receiver custom synopsis%n" +
                "Receiver Description Heading%n" +
                "Receiver description%n" +
                "Receiver Footer Heading%n" +
                "Receiver Footer%n");
        assertEquals(expected, usageString(commandLine, Help.Ansi.OFF));
    }

    static class SuperClassCommandAttributesDontOverwriteSubclassAttributes {
        @Command(name = "mixmein",
                version = "Mixin 1.0",
                separator = ":",
                description = "description from mixin",
                descriptionHeading = "Mixin Description Heading%n",
                header = "Mixin Header",
                headerHeading = "Mixin Header Heading%n",
                footer = "Mixin Footer",
                footerHeading = "Mixin Footer Heading%n",
                optionListHeading = "Mixin Option List Heading%n",
                parameterListHeading = "Mixin Parameter List Heading%n",
                commandListHeading = "Mixin Command List Heading%n",
                requiredOptionMarker = '%',
                synopsisHeading = "Mixin Synopsis Heading%n",
                abbreviateSynopsis = true,
                customSynopsis = "Mixin custom synopsis",
                showDefaultValues = true,
                sortOptions = false)
        static class MixMeInSuper {}

        @Command(name = "mixmeinSub",
                version = "MixinSub 1.0",
                separator = "~",
                description = "description from mixinSub",
                descriptionHeading = "MixinSub Description Heading%n",
                header = "MixinSub Header",
                headerHeading = "MixinSub Header Heading%n",
                footer = "MixinSub Footer",
                footerHeading = "MixinSub Footer Heading%n",
                optionListHeading = "MixinSub Option List Heading%n",
                parameterListHeading = "MixinSub Parameter List Heading%n",
                commandListHeading = "MixinSub Command List Heading%n",
                requiredOptionMarker = '#',
                synopsisHeading = "MixinSub Synopsis Heading%n",
                abbreviateSynopsis = true,
                customSynopsis = "MixinSub custom synopsis",
                showDefaultValues = true,
                sortOptions = false)
        static class MixMeInSub extends MixMeInSuper {}
    }

    @Test
    public void testMixinAnnotationSuperClassCommandAttributesDontOverwriteSubclassAttributes() throws Exception {
        class Receiver {
            @Mixin SuperClassCommandAttributesDontOverwriteSubclassAttributes.MixMeInSub mixMeIn;
        }

        CommandLine commandLine = new CommandLine(new Receiver());
        verifyMixinSuperClassCommandAttributesDontOverwriteSubclassAttributes(commandLine);
    }

    @Test
    public void testAddMixinSuperClassCommandAttributesDontOverwriteSubclassAttributes() throws Exception {
        @Command class Receiver {}

        CommandLine commandLine = new CommandLine(new Receiver());
        commandLine.addMixin("mixMeIn", new SuperClassCommandAttributesDontOverwriteSubclassAttributes.MixMeInSub());
        verifyMixinSuperClassCommandAttributesDontOverwriteSubclassAttributes(commandLine);
    }

    private void verifyMixinSuperClassCommandAttributesDontOverwriteSubclassAttributes(CommandLine commandLine) throws UnsupportedEncodingException {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals("mixmeinSub", commandSpec.name());
        assertArrayEquals(new String[] {"MixinSub 1.0"}, commandSpec.version());
        assertEquals("~", commandSpec.separator());
        assertArrayEquals(new String[] {"description from mixinSub"}, commandSpec.description());
        assertEquals("MixinSub Description Heading%n", commandSpec.descriptionHeading());
        assertArrayEquals(new String[] {"MixinSub Header"}, commandSpec.header());
        assertEquals("MixinSub Header Heading%n", commandSpec.headerHeading());
        assertArrayEquals(new String[] {"MixinSub Footer"}, commandSpec.footer());
        assertEquals("MixinSub Footer Heading%n", commandSpec.footerHeading());
        assertEquals("MixinSub Option List Heading%n", commandSpec.optionListHeading());
        assertEquals("MixinSub Parameter List Heading%n", commandSpec.parameterListHeading());
        assertEquals("MixinSub Command List Heading%n", commandSpec.commandListHeading());
        assertEquals('#', commandSpec.requiredOptionMarker());
        assertEquals("MixinSub Synopsis Heading%n", commandSpec.synopsisHeading());
        assertArrayEquals(new String[] {"MixinSub custom synopsis"}, commandSpec.customSynopsis());

        String expected = String.format("" +
                "MixinSub Header Heading%n" +
                "MixinSub Header%n" +
                "MixinSub Synopsis Heading%n" +
                "MixinSub custom synopsis%n" +
                "MixinSub Description Heading%n" +
                "description from mixinSub%n" +
                "MixinSub Footer Heading%n" +
                "MixinSub Footer%n");
        assertEquals(expected, usageString(commandLine, Help.Ansi.OFF));
    }

    static class CombinesAttributes {
        @Command(name = "superName",
                version = "MixMeInSuper 1.0",
                separator = "$",
                description = "33",
                descriptionHeading = "333",
                header = "3333",
                headerHeading = "33333",
                //footer = "333 3",
                //footerHeading = "333 33",
                //optionListHeading = "333 333",
                parameterListHeading = "333 333 3",
                commandListHeading = "333 333 33",
                requiredOptionMarker = '3',
                synopsisHeading = "3333 3")
        static class MixMeInSuper {}

        @Command(description = "description from mixinSub",
                descriptionHeading = "MixinSub Description Heading%n",
                header = "MixinSub Header",
                headerHeading = "MixinSub Header Heading%n",
                //footer = "222",
                //footerHeading = "222 222",
                //optionListHeading = "222 222 222",
                parameterListHeading = "2 22",
                commandListHeading = "222 2",
                requiredOptionMarker = '2',
                synopsisHeading = "22222")
        static class MixMeInSub extends MixMeInSuper {}

        @Command(
                //name = "000 - set by MixinMeInSuper",
                //version = "0.0 - set by MixinMeInSuper",
                //separator = "0 - set by MixinMeInSuper",
                //description = "00 - set by MixMeInSub",
                //descriptionHeading = "000 - set by MixMeInSub",
                //header = "0000 - set by MixMeInSub",
                //headerHeading = "00000 - set by MixMeInSub",

                footer = "ReceiverSuper Footer",
                footerHeading = "ReceiverSuper Footer Heading%n",
                optionListHeading = "ReceiverSuper Option List Heading%n",
                parameterListHeading = "-1-1-1",
                commandListHeading = "--1--1--1",
                requiredOptionMarker = '1',
                synopsisHeading = "---1---1---1")
        static class ReceiverSuper {}
    }

    @Test
    public void testMixinAnnotationCombinesAttributes() throws Exception {
        @Command(parameterListHeading = "Receiver Parameter List Heading%n",
                commandListHeading = "Receiver Command List Heading%n",
                requiredOptionMarker = '#',
                synopsisHeading = "Receiver Synopsis Heading%n",
                //customSynopsis = "Receiver custom synopsis", // use standard generated synopsis
                showDefaultValues = true,
                sortOptions = false)
        class Receiver extends CombinesAttributes.ReceiverSuper {
            @Mixin CombinesAttributes.MixMeInSub mixMeIn;
            @Parameters(description = "some files") File[] files;
        }

        CommandLine commandLine = new CommandLine(new Receiver());
        verifyMixinCombinesAttributes(commandLine);
    }

    @Test
    public void testAddMixinCombinesAttributes() throws Exception {
        @Command(parameterListHeading = "Receiver Parameter List Heading%n",
                commandListHeading = "Receiver Command List Heading%n",
                requiredOptionMarker = '#',
                synopsisHeading = "Receiver Synopsis Heading%n",
                //customSynopsis = "Receiver custom synopsis", // use standard generated synopsis
                showDefaultValues = true,
                sortOptions = false)
        class Receiver extends CombinesAttributes.ReceiverSuper {
            @Parameters(description = "some files") File[] files;
        }

        CommandLine commandLine = new CommandLine(new Receiver());
        commandLine.addMixin("mixMeIn", new CombinesAttributes.MixMeInSub());

        verifyMixinCombinesAttributes(commandLine);
    }

    private void verifyMixinCombinesAttributes(CommandLine commandLine) throws UnsupportedEncodingException {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals("superName", commandSpec.name());
        assertArrayEquals(new String[] {"MixMeInSuper 1.0"}, commandSpec.version());
        assertEquals("$", commandSpec.separator());

        assertArrayEquals(new String[] {"description from mixinSub"}, commandSpec.description());
        assertEquals("MixinSub Description Heading%n", commandSpec.descriptionHeading());
        assertArrayEquals(new String[] {"MixinSub Header"}, commandSpec.header());
        assertEquals("MixinSub Header Heading%n", commandSpec.headerHeading());

        assertArrayEquals(new String[] {"ReceiverSuper Footer"}, commandSpec.footer());
        assertEquals("ReceiverSuper Footer Heading%n", commandSpec.footerHeading());
        assertEquals("ReceiverSuper Option List Heading%n", commandSpec.optionListHeading());

        assertEquals("Receiver Parameter List Heading%n", commandSpec.parameterListHeading());
        assertEquals("Receiver Command List Heading%n", commandSpec.commandListHeading());
        assertEquals('#', commandSpec.requiredOptionMarker());
        assertEquals("Receiver Synopsis Heading%n", commandSpec.synopsisHeading());
        assertArrayEquals(new String[0], commandSpec.customSynopsis());

        String expected = String.format("" +
                "MixinSub Header Heading%n" +
                "MixinSub Header%n" +
                "Receiver Synopsis Heading%n" +
                "superName [<files>]...%n" +
                "MixinSub Description Heading%n" +
                "description from mixinSub%n" +
                "Receiver Parameter List Heading%n" +
                "      [<files>]...            some files%n" +
                "ReceiverSuper Footer Heading%n" +
                "ReceiverSuper Footer%n");
        assertEquals(expected, usageString(commandLine, Help.Ansi.OFF));
    }

    static class InjectsOptionsAndParameters {
        static class MixMeIn {
            @Option(names = {"-a", "--alpha"}, description = "option from mixin")
            private int alpha;

            @Parameters(description = "parameters from mixin")
            File[] files;
        }
    }
    @Test
    public void testMixinAnnotationInjectsOptionsAndParameters() throws UnsupportedEncodingException {
        @Command(sortOptions = false)
        class Receiver {
            @Option(names = {"-b", "--beta"}, description = "Receiver option")
            private int beta;

            @Parameters(description = "parameters from receiver")
            File[] receiverFiles;

            @Mixin
            InjectsOptionsAndParameters.MixMeIn mixMeIn;
        }
        CommandLine commandLine = new CommandLine(new Receiver());
        verifyMixinInjectsOptionsAndParameters(commandLine);
    }

    @Test
    public void testAddMixinInjectsOptionsAndParameters() throws UnsupportedEncodingException {
        @Command(sortOptions = false)
        class Receiver {
            @Option(names = {"-b", "--beta"}, description = "Receiver option")
            private int beta;

            @Parameters(description = "parameters from receiver")
            File[] receiverFiles;
        }
        CommandLine commandLine = new CommandLine(new Receiver());
        commandLine.addMixin("mixMeIn", new InjectsOptionsAndParameters.MixMeIn());
        verifyMixinInjectsOptionsAndParameters(commandLine);
    }

    private void verifyMixinInjectsOptionsAndParameters(CommandLine commandLine) throws UnsupportedEncodingException {
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals(2, commandSpec.options().size());
        assertArrayEquals(new String[]{"-b", "--beta"},  commandSpec.options().get(0).names());
        assertArrayEquals(new String[]{"-a", "--alpha"}, commandSpec.options().get(1).names());

        assertTrue(commandSpec.optionsMap().containsKey("--alpha"));
        assertTrue(commandSpec.optionsMap().containsKey("--beta"));
        assertTrue(commandSpec.optionsMap().containsKey("-a"));
        assertTrue(commandSpec.optionsMap().containsKey("-b"));

        assertTrue(commandSpec.posixOptionsMap().containsKey('a'));
        assertTrue(commandSpec.posixOptionsMap().containsKey('b'));

        assertEquals(2, commandSpec.positionalParameters().size());
        assertEquals("<receiverFiles>", commandSpec.positionalParameters().get(0).paramLabel());
        assertEquals("<files>",         commandSpec.positionalParameters().get(1).paramLabel());

        String expects = String.format("" +
                "Usage: <main class> [-a=<alpha>] [-b=<beta>] [<receiverFiles>]... [<files>]...%n" +
                "      [<receiverFiles>]...    parameters from receiver%n" +
                "      [<files>]...            parameters from mixin%n" +
                "  -b, --beta=<beta>           Receiver option%n" +
                "  -a, --alpha=<alpha>         option from mixin%n");
        assertEquals(expects, usageString(commandLine, Help.Ansi.OFF));
    }
    @Test
    public void testMixinAnnotationParsesOptionsAndParameters() throws UnsupportedEncodingException {
        @Command(sortOptions = false)
        class Receiver {
            @Option(names = {"-b", "--beta"}, description = "Receiver option")
            private int beta;

            @Parameters(description = "parameters from receiver")
            File[] receiverFiles;

            @Mixin
            InjectsOptionsAndParameters.MixMeIn mixMeIn;
        }
        CommandLine commandLine = new CommandLine(new Receiver());
        commandLine.parse("-a", "111", "-b", "222", "a", "b");
        Receiver receiver = commandLine.getCommand();
        assertEquals(222, receiver.beta);
        assertEquals(111, receiver.mixMeIn.alpha);
        assertArrayEquals(new File[] {new File("a"), new File("b")}, receiver.receiverFiles);
        assertArrayEquals(new File[] {new File("a"), new File("b")}, receiver.mixMeIn.files);
    }

    @Test
    public void testAddMixinParsesOptionsAndParameters() throws UnsupportedEncodingException {
        @Command(sortOptions = false)
        class Receiver {
            @Option(names = {"-b", "--beta"}, description = "Receiver option")
            private int beta;

            @Parameters(description = "parameters from receiver")
            File[] receiverFiles;
        }
        CommandLine commandLine = new CommandLine(new Receiver());
        InjectsOptionsAndParameters.MixMeIn mixin = new InjectsOptionsAndParameters.MixMeIn();
        commandLine.addMixin("mixin", mixin);

        commandLine.parse("-a", "111", "-b", "222", "a", "b");
        Receiver receiver = commandLine.getCommand();
        assertEquals(222, receiver.beta);
        assertEquals(111, mixin.alpha);
        assertArrayEquals(new File[] {new File("a"), new File("b")}, receiver.receiverFiles);
        assertArrayEquals(new File[] {new File("a"), new File("b")}, mixin.files);

        assertSame(mixin, commandLine.getMixins().get("mixin"));
        assertSame(mixin, commandLine.getCommandSpec().mixins().get("mixin").userObject());
    }

    @Test
    public void testMixinAnnotationInjectsOptionsAndParametersInDeclarationOrder() throws Exception {
        @Command(sortOptions = false)
        class Receiver {
            @Mixin
            InjectsOptionsAndParameters.MixMeIn mixMeIn;

            @Option(names = {"-b", "--beta"}, description = "Receiver option")
            private int beta;

            @Parameters(description = "parameters from receiver")
            File[] receiverFiles;
        }
        CommandLine commandLine = new CommandLine(new Receiver());
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertEquals(2, commandSpec.options().size());
        assertArrayEquals(new String[]{"-a", "--alpha"}, commandSpec.options().get(0).names());
        assertArrayEquals(new String[]{"-b", "--beta"},  commandSpec.options().get(1).names());

        assertEquals(2, commandSpec.positionalParameters().size());
        assertEquals("<files>",         commandSpec.positionalParameters().get(0).paramLabel());
        assertEquals("<receiverFiles>", commandSpec.positionalParameters().get(1).paramLabel());

        String expects = String.format("" +
                "Usage: <main class> [-a=<alpha>] [-b=<beta>] [<files>]... [<receiverFiles>]...%n" +
                "      [<files>]...            parameters from mixin%n" +
                "      [<receiverFiles>]...    parameters from receiver%n" +
                "  -a, --alpha=<alpha>         option from mixin%n" +
                "  -b, --beta=<beta>           Receiver option%n");
        assertEquals(expects, usageString(commandLine, Help.Ansi.OFF));
    }

    @Test
    public void testMixinAnnotationRejectsDuplicateOptions() {
        class MixMeInDuplicate {
            @Option(names = {"-a", "--alpha"}, description = "option from mixin")
            private int alpha;
        }

        class ReceiverDuplicate {
            @Option(names = {"-a"}, description = "Receiver option")
            private int beta;

            @Mixin
            MixMeInDuplicate mixMeIn;
        }
        try {
            new CommandLine(new ReceiverDuplicate(), new InnerClassFactory(this));
            fail("Expected exception");
        } catch (DuplicateOptionAnnotationsException ex) {
            assertEquals("Option name '-a' is used by both field int picocli.CommandLineMixinTest$1MixMeInDuplicate.alpha and field int picocli.CommandLineMixinTest$1ReceiverDuplicate.beta", ex.getMessage());
        }
    }

    @Test
    public void testMixinAnnotationWithSubcommands() {
        @Command(name = "mixinsub")
        class MixedInSubCommand {}

        @Command(subcommands = MixedInSubCommand.class)
        class MixMeIn {}

        class Receiver {
            @Mixin
            MixMeIn mixMeIn;
        }
        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        CommandSpec commandSpec = commandLine.getCommandSpec();

        assertEquals(1, commandLine.getSubcommands().size());
        assertEquals(1, commandSpec.subcommands().size());
        CommandLine subcommandLine = commandSpec.subcommands().get("mixinsub");
        assertSame(subcommandLine, commandLine.getSubcommands().get("mixinsub"));
        assertTrue(subcommandLine.getCommand() instanceof MixedInSubCommand);
    }

    @Test
    public void testMixinAnnotationWithVersionProvider() {
        class MyVersionProvider implements IVersionProvider {
            public String[] getVersion() {
                return new String[] {"line 1", "line 2"} ;
            }
        }
        @Command(version = "Mixin 1.0", versionProvider = MyVersionProvider.class)
        class MixMeIn {}

        class Receiver {
            @Mixin MixMeIn mixMeIn;
        }

        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        CommandSpec commandSpec = commandLine.getCommandSpec();
        assertTrue(commandSpec.versionProvider() instanceof MyVersionProvider);
        assertArrayEquals(new String[] {"line 1", "line 2"}, commandSpec.version());
    }

    @Test
    public void testMixinAnnotationCanBeRetrievedByAnnotationName() {
        @Command class MixMeIn {}

        @Command class Receiver {
            @Mixin(name = "aMixin") MixMeIn mixMeIn;
        }
        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        assertFalse("mixin was registered", commandLine.getMixins().isEmpty());
        assertTrue(commandLine.getMixins().get("aMixin") instanceof MixMeIn);

        Receiver receiver = commandLine.getCommand();
        assertNotNull(receiver.mixMeIn);
        assertSame(receiver.mixMeIn, commandLine.getMixins().get("aMixin"));
        assertSame(receiver.mixMeIn, commandLine.getCommandSpec().mixins().get("aMixin").userObject());
    }

    @Test
    public void testMixinAnnotationCanBeRetrievedByFieldName() {
        @Command class MixMeIn {}

        @Command class Receiver {
            @Mixin MixMeIn mixMeIn;
        }
        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        assertFalse("mixin was registered", commandLine.getMixins().isEmpty());
        assertTrue(commandLine.getMixins().get("mixMeIn") instanceof MixMeIn);

        Receiver receiver = commandLine.getCommand();
        assertNotNull(receiver.mixMeIn);
        assertSame(receiver.mixMeIn, commandLine.getMixins().get("mixMeIn"));
        assertSame(receiver.mixMeIn, commandLine.getCommandSpec().mixins().get("mixMeIn").userObject());
    }

    @Test
    public void testAddMixin_CanBeRetrievedByFieldName() {
        @Command class MixMeIn {}
        @Command class Receiver {}

        CommandLine commandLine = new CommandLine(new Receiver(), new InnerClassFactory(this));
        commandLine.addMixin("mixin", new MixMeIn());
        assertFalse("mixin was registered", commandLine.getMixins().isEmpty());
        assertTrue(commandLine.getMixins().get("mixin") instanceof MixMeIn);
    }

}
