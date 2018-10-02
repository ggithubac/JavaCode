Instruction
The goal is to identify a crashing or exception bug and provide a fix in the following pseudo code. There is a single producer and multiple consumers. The producer puts items on the queue, while the consumers remove items from queue and process them. condition.signal() will wake up one of the blocked consumers waiting on the condition. While waiting in blocked state, wait() will release the mutex. Upon receiving the signal, wait() will unblock and re-acquire the mutex. There are sufficient consumers to process the items where the queue will not build up. Context switch can occur anywhere in the code. Please describe how the bug can occur and provide a fix. Please submit the solution to exercise@nyansa.com.

```
Producer
while (true) {
       item = generate_item()
       mutex.lock()
       fifo_queue.push(item)
       mutex.unlock()
       condition.signal()
}

Consumers
while(true) {
       mutex.lock()
       if (fifo_queue.empty()) {
             condition.wait(mutex)
       }
       item = fifo_queue.remove()
       mutex.unlock()
       process_item(item)
}
```

# Solution:
  The bug in the code is as follows:
   let us say that the fifo_queue is empty for now and since there are many consumers, lets say two consumers(c1, c2) try to acquire the mutex lock. One consumer(c1) gets the lock, the second consumer(c2) waits on the mutex.lock(). c1 finds the queue empty and waits on the condition. c1 releases the lock but continues to wait on the blocked condition. Now lets say producer(p) successfully acquires the lock and adds the item to the fifo queue, and unlocks the mutex lock, before it sends the condition.signal, lets say context switch happens and c2 acquires the lock. At this point c1 is still waiting on the blocked condition, but c2 after acquiring the lock sees that fifo queue is not empty and removes the item from fifo queue and unlocks the item and processes the item. At this time if producer p get a chance to run and sends the condition.signal() c1 which was blocking on this condition wakes up and trys to remove item from a empty fifo queue unlocks the mutex and while processing the null item will crash.  This is the bug in the code. The way to fix is as below:



```
Producer
while (true) {
       item = generate_item()
       mutex.lock()
       fifo_queue.push(item)
       mutex.unlock()
       condition.signal()
}

Consumers
while(true) {
       mutex.lock()
       while (fifo_queue.empty()) { <-- change the if condition to while loop
             condition.wait(mutex)
       }
       item = fifo_queue.remove()
       mutex.unlock()
       process_item(item)
}
```

# Fix:
  Fix is that when the blocked consumers are waiting on the signal and gets unblocked it has to check again if the queue is empty before dequeuing it and trying to process the item, as other consumers might have processed the item and queue is back to empty state. By converting if to while after condition.wait(mutex) the consumer will check again if fifo_queue.empty()  and wait on the condition again if it finds the queue empty.

