package com.iwdael.taskcenter.core;

import com.google.gson.Gson;
import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.util.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskProcessingCenter {
    private final Node<Object, Object> root;
    private final Map<Integer, NodeInformation> nodeSource;

    public TaskProcessingCenter(Node<Object, Object> root) {
        this.root = root;
        this.nodeSource = new HashMap<>();
    }

    private synchronized NodeInformation obtainInformation(Node<Object, Object> node) {
        NodeInformation nodeInformation = this.nodeSource.get(node.index);
        if (nodeInformation == null) {
            nodeInformation = new NodeInformation();
            this.nodeSource.put(node.index, nodeInformation);
        }
        return nodeInformation;
    }

    protected synchronized void initialize(Node<Object, Object> node, Object source) {
        if (this.obtainInformation(node).contain(source)) return;
        this.obtainInformation(node).add(new Information(source));
    }

    public synchronized void handling(Node<Object, Object> node, Object source) {
        if (!this.obtainInformation(node).contain(source)) throw new IllegalStateException("not found source:" + source);
        List<Information> information = this.obtainInformation(node);
        for (Information info : information) {
            if (source != info.source) continue;
            info.handle = Information.HANDLING;
        }
    }

    protected synchronized void handled(Node<Object, Object> node, Object source) {
        if (!this.obtainInformation(node).contain(source)) throw new IllegalStateException("not found source:" + source);
        List<Information> information = this.obtainInformation(node);
        for (Information info : information) {
            if (source != info.source) continue;
            info.handle = Information.HANDLED;
        }
    }

    protected synchronized void dispatch() {
        Node<Object, Object> node = root;
        while (node != null) {
            if (node instanceof NodeInterceptor) {
                if (!isHandled(node)) break;
                int initialSize = this.obtainInformation(node).getCount(Information.INITIALIZE);
                int handledSize = this.obtainInformation(node).getCount(Information.HANDLED);
                int informationSize = this.obtainInformation(node).size();
                if (initialSize == informationSize) {
                    Collection<Object> information = this.obtainInformation(node).sources(Information.INITIALIZE);
                    TaskCenter.executors().submit(new TaskInterceptRunnable(this, node, information));
                    break;
                } else if (handledSize != informationSize) {
                    break;
                }
            } else {
                List<Information> information = this.obtainInformation(node).information(Information.INITIALIZE);
                for (Information info : information) {
                    Runnable task;
                    if (node instanceof NodeMapper) {
                        task = new TaskMapRunnable(this, node, info.source);
                    } else if (node instanceof NodeDisperser) {
                        task = new TaskDisperseRunnable(this, node, info.source);
                    } else if (node instanceof NodeClosure) {
                        task = new TaskCloseRunnable(this, node, info.source);
                    } else {
                        task = new TaskUnitRunnable(this, node, info.source);
                    }
                    TaskCenter.executors().submit(task);
                }

            }
            node = node.next;
        }
//        Logger.tag("hand").v(new Gson().toJson(nodeSource));
        Logger.tag("hand").v("handleAll:" + isHandled());
    }

    protected synchronized boolean isHandled() {
        Node<Object, Object> node = root;
        while (node != null) {
            List<Information> information = obtainInformation(node);
            for (Information info : information) {
                if (info.handle != Information.HANDLED) return false;
            }
            node = node.next;
        }
        return true;
    }


    protected synchronized boolean isHandled(Node<Object, Object> target) {
        Node<Object, Object> node = root;
        while (node != target) {
            List<Information> information = obtainInformation(node);
            for (Information info : information) {
                if (info.handle != Information.HANDLED) return false;
            }
            node = node.next;
        }
        return true;
    }

    protected static class Information {
        private static final int INITIALIZE = 1;
        private static final int HANDLING = 2;
        private static final int HANDLED = 3;
        public final Object source;
        public int handle = INITIALIZE;

        public Information(Object source) {
            this.source = source;
        }
    }

    private static class NodeInformation extends ArrayList<Information> {
        private int getCount(int handle) {
            int count = 0;
            for (Information information : this) {
                if (information.handle == handle) count++;
            }
            return count;
        }

        private List<Object> sources(int handle) {
            List<Object> objects = new ArrayList<>();
            for (Information information : this) {
                if (information.handle == handle) {
                    objects.add(information.source);
                }
            }
            return objects;
        }

        private List<Information> information(int handle) {
            List<Information> objects = new ArrayList<>();
            for (Information information : this) {
                if (information.handle == handle) {
                    objects.add(information);
                }
            }
            return objects;
        }

        private boolean contain(Object o) {
            for (Information information : this) {
                if (Objects.equals(information.source, o)) return true;
            }
            return false;
        }
    }

}
