package edu.lu.uni.serval.gumtree.regroup;

import java.util.List;

import edu.lu.uni.serval.diffentry.DiffEntryHunk;

public class HunkFixPattern {
	
	private DiffEntryHunk hunk;
	
	private List<HierarchicalActionSet> hunkActionSets;

	public HunkFixPattern(DiffEntryHunk hunk, List<HierarchicalActionSet> hunkActionSets) {
		super();
		this.hunk = hunk;
		this.hunkActionSets = hunkActionSets;
	}

	public DiffEntryHunk getHunk() {
		return hunk;
	}

	public List<HierarchicalActionSet> getHunkActionSets() {
		return hunkActionSets;
	}
	
}
