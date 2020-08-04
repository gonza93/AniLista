package redix.soft.anilist.util;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.Map;
import java.util.Stack;

public class FragmentStack implements Serializable {

    private transient Map<NavigationUtil.TAB, Stack<Fragment>> fragmentStacks;

    public Map<NavigationUtil.TAB, Stack<Fragment>> getFragmentStacks() {
        return fragmentStacks;
    }

    public void setFragmentStacks(Map<NavigationUtil.TAB, Stack<Fragment>> fragmentStacks) {
        this.fragmentStacks = fragmentStacks;
    }
}
