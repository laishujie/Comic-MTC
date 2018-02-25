package com.lai.mtc.mvp.utlis;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lai
 * @time 2018/1/14 15:30
 * @describe describe
 */

public class FragmentUtils {
    private static final int TYPE_ADD_FRAGMENT = 1;
    private static final int TYPE_HIDE_ADD_FRAGMENT = 2;
    private static final int TYPE_REMOVE_FRAGMENT = 4;
    private static final int TYPE_REMOVE_TO_FRAGMENT = 8;
    private static final int TYPE_REPLACE_FRAGMENT = 16;
    private static final int TYPE_POP_ADD_FRAGMENT = 32;
    private static final int TYPE_HIDE_FRAGMENT = 64;
    private static final int TYPE_SHOW_FRAGMENT = 128;
    private static final int TYPE_HIDE_SHOW_FRAGMENT = 256;
    private static final String ARGS_ID = "args_id";
    private static final String ARGS_IS_HIDE = "args_is_hide";
    private static final String ARGS_IS_ADD_STACK = "args_is_add_stack";

    private FragmentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Fragment addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId) {
        return addFragment(fragmentManager, fragment, containerId, false);
    }

    public static Fragment addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isHide) {
        return addFragment(fragmentManager, fragment, containerId, isHide, false);
    }

    public static Fragment addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isHide, boolean isAddStack) {
        putArgs(fragment, new FragmentUtils.Args(containerId, isHide, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 1, new FragmentUtils.SharedElement[0]);
    }

    public static Fragment addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isHide, boolean isAddStack, FragmentUtils.SharedElement... sharedElement) {
        putArgs(fragment, new FragmentUtils.Args(containerId, isHide, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 1, sharedElement);
    }

    public static Fragment hideAddFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment hideFragment, @NonNull Fragment addFragment, @IdRes int containerId, boolean isHide, boolean isAddStack, FragmentUtils.SharedElement... sharedElement) {
        putArgs(addFragment, new FragmentUtils.Args(containerId, isHide, isAddStack));
        return operateFragment(fragmentManager, hideFragment, addFragment, 2, sharedElement);
    }

    public static Fragment addFragments(@NonNull FragmentManager fragmentManager, @NonNull List<Fragment> fragments, @IdRes int containerId, int showIndex) {
        int i = 0;

        for(int size = fragments.size(); i < size; ++i) {
            Fragment fragment = (Fragment)fragments.get(i);
            if(fragment != null) {
                addFragment(fragmentManager, fragment, containerId, showIndex != i, false);
            }
        }

        return (Fragment)fragments.get(showIndex);
    }

    public static Fragment addFragments(@NonNull FragmentManager fragmentManager, @NonNull List<Fragment> fragments, @IdRes int containerId, int showIndex, @NonNull List... lists) {
        int i = 0;

        for(int size = fragments.size(); i < size; ++i) {
            Fragment fragment = (Fragment)fragments.get(i);
            List<FragmentUtils.SharedElement> list = lists[i];
            if(fragment != null && list != null) {
                putArgs(fragment, new FragmentUtils.Args(containerId, showIndex != i, false));
                return operateFragment(fragmentManager, (Fragment)null, fragment, 1, (FragmentUtils.SharedElement[])list.toArray(new FragmentUtils.SharedElement[0]));
            }
        }

        return (Fragment)fragments.get(showIndex);
    }

    public static void removeFragment(@NonNull Fragment fragment) {
        operateFragment(fragment.getFragmentManager(), (Fragment)null, fragment, 4, new FragmentUtils.SharedElement[0]);
    }

    public static void removeToFragment(@NonNull Fragment fragment, boolean isIncludeSelf) {
        operateFragment(fragment.getFragmentManager(), isIncludeSelf?fragment:null, fragment, 8, new FragmentUtils.SharedElement[0]);
    }

    public static void removeFragments(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(!fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    removeFragment(fragment);
                }
            }

        }
    }

    public static void removeAllFragments(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(!fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    removeAllFragments(fragment.getChildFragmentManager());
                    removeFragment(fragment);
                }
            }

        }
    }

    public static Fragment replaceFragment(@NonNull Fragment srcFragment, @NonNull Fragment destFragment) {
        return replaceFragment(srcFragment, destFragment, false);
    }

    public static Fragment replaceFragment(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack) {
        if(srcFragment.getArguments() == null) {
            return null;
        } else {
            int containerId = srcFragment.getArguments().getInt("args_id");
            return containerId == 0?null:replaceFragment(srcFragment.getFragmentManager(), destFragment, containerId, isAddStack);
        }
    }

    public static Fragment replaceFragment(@NonNull Fragment srcFragment, @NonNull Fragment destFragment, boolean isAddStack, FragmentUtils.SharedElement... sharedElement) {
        if(srcFragment.getArguments() == null) {
            return null;
        } else {
            int containerId = srcFragment.getArguments().getInt("args_id");
            return containerId == 0?null:replaceFragment(srcFragment.getFragmentManager(), destFragment, containerId, isAddStack, sharedElement);
        }
    }

    public static Fragment replaceFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack) {
        putArgs(fragment, new FragmentUtils.Args(containerId, false, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 16, new FragmentUtils.SharedElement[0]);
    }

    public static Fragment replaceFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack, FragmentUtils.SharedElement... sharedElement) {
        putArgs(fragment, new FragmentUtils.Args(containerId, false, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 16, sharedElement);
    }

    public static boolean popFragment(@NonNull FragmentManager fragmentManager) {
        return fragmentManager.popBackStackImmediate();
    }

    public static boolean popToFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass, boolean isIncludeSelf) {
        return fragmentManager.popBackStackImmediate(fragmentClass.getName(), isIncludeSelf?1:0);
    }

    public static void popFragments(@NonNull FragmentManager fragmentManager) {
        while(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

    }

    public static void popAllFragments(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(!fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    popAllFragments(fragment.getChildFragmentManager());
                }
            }

            while(fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

        }
    }

    public static Fragment popAddFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack) {
        putArgs(fragment, new FragmentUtils.Args(containerId, false, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 32, new FragmentUtils.SharedElement[0]);
    }

    public static Fragment popAddFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int containerId, boolean isAddStack, FragmentUtils.SharedElement... sharedElements) {
        putArgs(fragment, new FragmentUtils.Args(containerId, false, isAddStack));
        return operateFragment(fragmentManager, (Fragment)null, fragment, 32, sharedElements);
    }

    public static Fragment hideFragment(@NonNull Fragment fragment) {
        FragmentUtils.Args args = getArgs(fragment);
        if(args != null) {
            putArgs(fragment, new FragmentUtils.Args(args.id, true, args.isAddStack));
        }

        return operateFragment(fragment.getFragmentManager(), (Fragment)null, fragment, 64, new FragmentUtils.SharedElement[0]);
    }

    public static void hideFragments(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(!fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    hideFragment(fragment);
                }
            }

        }
    }

    public static Fragment showFragment(@NonNull Fragment fragment) {
        FragmentUtils.Args args = getArgs(fragment);
        if(args != null) {
            putArgs(fragment, new FragmentUtils.Args(args.id, false, args.isAddStack));
        }

        return operateFragment(fragment.getFragmentManager(), (Fragment)null, fragment, 128, new FragmentUtils.SharedElement[0]);
    }

    public static Fragment hideAllShowFragment(@NonNull Fragment fragment) {
        hideFragments(fragment.getFragmentManager());
        return operateFragment(fragment.getFragmentManager(), (Fragment)null, fragment, 128, new FragmentUtils.SharedElement[0]);
    }

    public static Fragment hideShowFragment(@NonNull Fragment hideFragment, @NonNull Fragment showFragment) {
        FragmentUtils.Args args = getArgs(hideFragment);
        if(args != null) {
            putArgs(hideFragment, new FragmentUtils.Args(args.id, true, args.isAddStack));
        }

        args = getArgs(showFragment);
        if(args != null) {
            putArgs(showFragment, new FragmentUtils.Args(args.id, false, args.isAddStack));
        }

        return operateFragment(showFragment.getFragmentManager(), hideFragment, showFragment, 256, new FragmentUtils.SharedElement[0]);
    }

    private static void putArgs(@NonNull Fragment fragment, FragmentUtils.Args args) {
        Bundle bundle = fragment.getArguments();
        if(bundle == null) {
            bundle = new Bundle();
            fragment.setArguments(bundle);
        }

        bundle.putInt("args_id", args.id);
        bundle.putBoolean("args_is_hide", args.isHide);
        bundle.putBoolean("args_is_add_stack", args.isAddStack);
    }

    private static FragmentUtils.Args getArgs(@NonNull Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        return bundle != null && bundle.getInt("args_id") != 0?new FragmentUtils.Args(bundle.getInt("args_id"), bundle.getBoolean("args_is_hide"), bundle.getBoolean("args_is_add_stack")):null;
    }

    private static Fragment operateFragment(@NonNull FragmentManager fragmentManager, Fragment srcFragment, @NonNull Fragment destFragment, int type, FragmentUtils.SharedElement... sharedElements) {
        if(srcFragment == destFragment) {
            return null;
        } else if(srcFragment != null && srcFragment.isRemoving()) {
            //LogUtils.e(srcFragment.getClass().getName() + " is isRemoving");
            return null;
        } else {
            String name = destFragment.getClass().getName();
            Bundle args = destFragment.getArguments();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            int i;
            if(sharedElements != null && sharedElements.length != 0) {
                FragmentUtils.SharedElement[] var8 = sharedElements;
                int var9 = sharedElements.length;

                for(i = 0; i < var9; ++i) {
                    FragmentUtils.SharedElement element = var8[i];
                    ft.addSharedElement(element.sharedElement, element.name);
                }
            } else {
                ft.setTransition(4097);
            }

            label56:
            switch(type) {
                case 2:
                    ft.hide(srcFragment);
                case 1:
                    Fragment fragmentByTag = fragmentManager.findFragmentByTag(name);
                    if(fragmentByTag != null) {
                        destFragment = fragmentByTag;
                    }

                    ft.add(args.getInt("args_id"), destFragment, name);
                    if(args.getBoolean("args_is_hide")) {
                        ft.hide(destFragment);
                    }

                    if(args.getBoolean("args_is_add_stack")) {
                        ft.addToBackStack(name);
                    }
                    break;
                case 4:
                    ft.remove(destFragment);
                    break;
                case 8:
                    List<Fragment> fragments = getFragments(fragmentManager);
                    i = fragments.size() - 1;

                    while(true) {
                        if(i < 0) {
                            break label56;
                        }

                        Fragment fragment = (Fragment)fragments.get(i);
                        if(fragment == destFragment) {
                            if(srcFragment != null) {
                                ft.remove(fragment);
                            }
                            break label56;
                        }

                        ft.remove(fragment);
                        --i;
                    }
                case 16:
                    ft.replace(args.getInt("args_id"), destFragment, name);
                    if(args.getBoolean("args_is_add_stack")) {
                        ft.addToBackStack(name);
                    }
                    break;
                case 32:
                    popFragment(fragmentManager);
                    ft.add(args.getInt("args_id"), destFragment, name);
                    if(args.getBoolean("args_is_add_stack")) {
                        ft.addToBackStack(name);
                    }
                    break;
                case 64:
                    ft.hide(destFragment);
                    break;
                case 128:
                    ft.show(destFragment);
                    break;
                case 256:
                    ft.hide(srcFragment).show(destFragment);
            }

            ft.commitAllowingStateLoss();
            return destFragment;
        }
    }

    public static Fragment getLastAddFragment(@NonNull FragmentManager fragmentManager) {
        return getLastAddFragmentIsInStack(fragmentManager, false);
    }

    public static Fragment getLastAddFragmentInStack(@NonNull FragmentManager fragmentManager) {
        return getLastAddFragmentIsInStack(fragmentManager, true);
    }

    private static Fragment getLastAddFragmentIsInStack(@NonNull FragmentManager fragmentManager, boolean isInStack) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(fragments.isEmpty()) {
            return null;
        } else {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    if(!isInStack) {
                        return fragment;
                    }

                    if(fragment.getArguments().getBoolean("args_is_add_stack")) {
                        return fragment;
                    }
                }
            }

            return null;
        }
    }

    public static Fragment getTopShowFragment(@NonNull FragmentManager fragmentManager) {
        return getTopShowFragmentIsInStack(fragmentManager, (Fragment)null, false);
    }

    public static Fragment getTopShowFragmentInStack(@NonNull FragmentManager fragmentManager) {
        return getTopShowFragmentIsInStack(fragmentManager, (Fragment)null, true);
    }

    private static Fragment getTopShowFragmentIsInStack(@NonNull FragmentManager fragmentManager, Fragment parentFragment, boolean isInStack) {
        List<Fragment> fragments = getFragments(fragmentManager);
        if(fragments.isEmpty()) {
            return parentFragment;
        } else {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null && fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint()) {
                    if(!isInStack) {
                        return getTopShowFragmentIsInStack(fragment.getChildFragmentManager(), fragment, false);
                    }

                    if(fragment.getArguments().getBoolean("args_is_add_stack")) {
                        return getTopShowFragmentIsInStack(fragment.getChildFragmentManager(), fragment, true);
                    }
                }
            }

            return parentFragment;
        }
    }

    public static List<Fragment> getFragments(@NonNull FragmentManager fragmentManager) {
        return getFragmentsIsInStack(fragmentManager, false);
    }

    public static List<Fragment> getFragmentsInStack(@NonNull FragmentManager fragmentManager) {
        return getFragmentsIsInStack(fragmentManager, true);
    }

    private static List<Fragment> getFragmentsIsInStack(@NonNull FragmentManager fragmentManager, boolean isInStack) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null && !fragments.isEmpty()) {
            List<Fragment> result = new ArrayList();

            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    if(isInStack) {
                        if(fragment.getArguments().getBoolean("args_is_add_stack")) {
                            result.add(fragment);
                        }
                    } else {
                        result.add(fragment);
                    }
                }
            }

            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public static List<FragmentUtils.FragmentNode> getAllFragments(@NonNull FragmentManager fragmentManager) {
        return getAllFragmentsIsInStack(fragmentManager, new ArrayList(), false);
    }

    public static List<FragmentUtils.FragmentNode> getAllFragmentsInStack(@NonNull FragmentManager fragmentManager) {
        return getAllFragmentsIsInStack(fragmentManager, new ArrayList(), true);
    }

    private static List<FragmentUtils.FragmentNode> getAllFragmentsIsInStack(@NonNull FragmentManager fragmentManager, List<FragmentUtils.FragmentNode> result, boolean isInStack) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null && !fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null) {
                    if(isInStack) {
                        if(fragment.getArguments().getBoolean("args_is_add_stack")) {
                            result.add(new FragmentUtils.FragmentNode(fragment, getAllFragmentsIsInStack(fragment.getChildFragmentManager(), new ArrayList(), true)));
                        }
                    } else {
                        result.add(new FragmentUtils.FragmentNode(fragment, getAllFragmentsIsInStack(fragment.getChildFragmentManager(), new ArrayList(), false)));
                    }
                }
            }

            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public static Fragment getPreFragment(@NonNull Fragment destFragment) {
        FragmentManager fragmentManager = destFragment.getFragmentManager();
        if(fragmentManager == null) {
            return null;
        } else {
            List<Fragment> fragments = getFragments(fragmentManager);
            boolean flag = false;

            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(flag && fragment != null) {
                    return fragment;
                }

                if(fragment == destFragment) {
                    flag = true;
                }
            }

            return null;
        }
    }

    public static Fragment findFragment(@NonNull FragmentManager fragmentManager, Class<? extends Fragment> fragmentClass) {
        List<Fragment> fragments = getFragments(fragmentManager);
        return fragments.isEmpty()?null:fragmentManager.findFragmentByTag(fragmentClass.getName());
    }

    public static boolean dispatchBackPress(@NonNull Fragment fragment) {
        return dispatchBackPress(fragment.getFragmentManager());
    }

    public static boolean dispatchBackPress(@NonNull FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null && !fragments.isEmpty()) {
            for(int i = fragments.size() - 1; i >= 0; --i) {
                Fragment fragment = (Fragment)fragments.get(i);
                if(fragment != null && fragment.isResumed() && fragment.isVisible() && fragment.getUserVisibleHint() && fragment instanceof FragmentUtils.OnBackClickListener && ((FragmentUtils.OnBackClickListener)fragment).onBackClick()) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static void setBackgroundColor(@NonNull Fragment fragment, @ColorInt int color) {
        View view = fragment.getView();
        if(view != null) {
            view.setBackgroundColor(color);
        }

    }

    public static void setBackgroundResource(@NonNull Fragment fragment, @DrawableRes int resId) {
        View view = fragment.getView();
        if(view != null) {
            view.setBackgroundResource(resId);
        }

    }

    public static void setBackground(@NonNull Fragment fragment, Drawable background) {
        ViewCompat.setBackground(fragment.getView(), background);
    }

    public interface OnBackClickListener {
        boolean onBackClick();
    }

    public static class FragmentNode {
        Fragment fragment;
        List<FragmentUtils.FragmentNode> next;

        public FragmentNode(Fragment fragment, List<FragmentUtils.FragmentNode> next) {
            this.fragment = fragment;
            this.next = next;
        }

        public String toString() {
            return this.fragment.getClass().getSimpleName() + "->" + (this.next != null && !this.next.isEmpty()?this.next.toString():"no child");
        }
    }

    public static class SharedElement {
        View sharedElement;
        String name;

        public SharedElement(View sharedElement, String name) {
            this.sharedElement = sharedElement;
            this.name = name;
        }
    }

    static class Args {
        int id;
        boolean isHide;
        boolean isAddStack;

        private Args(int id, boolean isHide, boolean isAddStack) {
            this.id = id;
            this.isHide = isHide;
            this.isAddStack = isAddStack;
        }
    }
}
