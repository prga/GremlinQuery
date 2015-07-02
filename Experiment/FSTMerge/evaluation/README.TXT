After all merge operations have been applied, we use several scripts and programs to
evaluate our approach.

First of all we sum up all merge conflicts that occur in a software system. Therefor,
apply the script
  ./writeAllResults-JAVA.sh ../examples/jEdit

(./writeResults-PY.sh and ./writeResults-CS.sh for python respectively csharp projects).
The script creates two csv files (one for unstructured merge and one for semistructured
merge).

The csv are created in <projectname> in the evaluation folder. We compare semistructured
merge with unstructured merge using two R scripts (Percentage.R and Diagram.R). Run both
commands in the evaluation folder with:
  R -f Percentage.R

and
  R -f Diagram.R

