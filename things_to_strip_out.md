## Thread ID line

done '^".*[0-9]+.*"' - subsitute all numbers thread title e.g. "TP-Processor50", "pool-2-thread-1", "BrokerService[messageQueueService] Task-2804" with "N"
done 'prio=[0-9]+'
done 'tid=0x[0-9a-f]+'
done 'nid=0x[0-9a-f]+'
done '\[0x[0-9a-f]+\]'

## Locks

done '-\\s.*<0x[0-9a-f]+> \(a '

## Prefixes

- `jvm [0-9]+\s+|` i.e. `jvm 1    |`
