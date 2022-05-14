package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static Double _defaultSteps = 150.0;
	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;
	
	private static Double nSteps = null;
	private static String _expOutFile = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		// TODO initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		
		// TODO initialize the force laws factory
		ArrayList<Builder<ForceLaws>> flBuilders = new ArrayList<>();
		flBuilders.add(new NewtonUniversalGravitationBuilder());
		flBuilders.add(new MovingTowardsFixedPointBuilder());
		flBuilders.add(new NoForceBuilder());	
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(flBuilders);

		// TODO initialize the state comparator
		ArrayList<Builder<StateComparator>> scBuilders = new ArrayList<>();
		scBuilders.add(new MassEqualStateBuilder());
		scBuilders.add(new EpsilonEqualStateBuilder());	
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(scBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			// TODO add support of -o, -eo, and -s (define corresponding parse methods)
			parseOutputOption(line);
			parseExpectedOutputOption(line);
			parseStepsOption(line);
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseBatchModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// TODO add support for -o, -eo, and -s (add corresponding information to
		// cmdLineOptions)

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());
		
		// -eo (expected output)
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value:" 
						+ _dtimeDefaultValue + ".").build());
		
		// -o (output)
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc(
				"Output file, where output is written. Default value: the standard output.").build()); // OJO CON LO DE STANDARD VALUE
		
		// -s (steps)
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc(
				 "An integer representing the number of simulation steps. Default value:" + _defaultSteps + ".").build());
		
		// -m (mode batch)
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				 "Execution Mode. Possible values: ’batch’ (Batch mode), ’gui’ (Graphical User Interface mode). Default value. 'Batch'").build());
						
		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}
	
	private static void parseExpectedOutputOption(CommandLine line) throws ParseException{
		_expOutFile = line.getOptionValue("eo");
	}
	
	private static void parseOutputOption(CommandLine line) throws ParseException{
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseBatchModeOption(CommandLine line) throws ParseException{
		_mode = line.getOptionValue("m");
		if (_mode.equals("batch")) {
			_mode = "batch";
		}
		else {
			_mode = "gui";
		}
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException{
		String stps = line.getOptionValue("s", _defaultSteps.toString());
		try {
			nSteps = Double.parseDouble(stps);
			assert (nSteps > 0);
		} catch(Exception e) {
			throw new ParseException("Invalid number of steps: " + nSteps);
		}
	}


	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os = _outFile == null ?
				System.out : new FileOutputStream(new File(_outFile));
		ForceLaws fLaws = _forceLawsFactory.createInstance(_forceLawsInfo);
		
		// Crear el simulador
		PhysicsSimulator sim = new PhysicsSimulator(_dtime, fLaws); 
		// Crear el controlador
		Controller ctrl = new Controller(sim, _bodyFactory, _forceLawsFactory);
		ctrl.loadBodies(is);
		
		InputStream expOut = null;
		StateComparator stateCmp = null;
		if (_expOutFile != null) {
			expOut = new FileInputStream(new File(_expOutFile));
			stateCmp = _stateComparatorFactory.createInstance(_stateComparatorInfo); 
		}
		
		ctrl.run(nSteps, os, expOut, stateCmp);
	}
	
	private static void startGuiMode() throws Exception{
		
		ForceLaws fLaws = _forceLawsFactory.createInstance(_forceLawsInfo); 
		PhysicsSimulator sim = new PhysicsSimulator(_dtime, fLaws);
		Controller ctlr = new Controller(sim, _bodyFactory, _forceLawsFactory);
		if (_inFile != null) {
			InputStream is = new FileInputStream(new File(_inFile));
			ctlr.loadBodies(is);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow(ctlr);
			}
		});
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if (_mode == "batch") startBatchMode();
		else startGuiMode();
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
