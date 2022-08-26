local name=KEYS[1]
local exist=redis.call('exists',name)
if exist then
    redis.call('del',name)
end

