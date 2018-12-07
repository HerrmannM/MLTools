package mltools.arff;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/** Given a set of ArffFile, create a reader merging those files. They should all have the same structure. */
public class ArffFileJoin implements ArffReaderFactory {

	// --- --- --- Constructor
	private final int nbFiles;
	private final int numData;
	private final Path[] sources;
	private final Instances structure;
	
	public ArffFileJoin(ArffFile[] files) throws IOException {
		nbFiles = files.length;
		
		int ndata = 0;
		sources = new Path[nbFiles];
		
		for(int i=0; i<nbFiles; ++i) {
			ArffFile f = files[i];
			ndata += f.getNumData();
			sources[i] = f.getPath();
		}
		
		numData = ndata;
		structure = files[0].getStructure();
	}

	// --- --- --- Methods

	/** Create a new ArffReader for the associated file */
	@Override
	public ArffReader getNewReader() throws IOException {
		Reader readers[] = new Reader[nbFiles];
		for(int i=0; i<nbFiles; ++i) {
			readers[i] = Utility.getJavaReader(sources[i]);
		}
		return new ArffJoinReader(readers, structure);
	}

	@Override
	public int getNumData() {
		return numData;
	}

	@Override
	public Instances getStructure() {
		return structure;
	}

	@Override
	public String getDatasetName() {
		return sources[0].getFileName().toString();
	}


}