To invoke the merge algorithm, the FeatureHouse tool has to be invoked as follows:
  java -cp featurehouse.jar merger.FSTGenMerger --expression <configuration file>

The configuration file must contain exactly three entries representing the first
variant, the base program, and the second variant (in top-down order).
  java -cp featurehouse.jar merger.FSTGenMerger --expression ../examples/jEdit/rev4676-4998/rev4676-4998.revisions

Beware a single run can take a huge amount of time (several hours for large projects).
After running the program the results are in the folder ../examples/jEdit/rev4676/rev4676-4998.
For evaluation switch into the evaluation folder.
