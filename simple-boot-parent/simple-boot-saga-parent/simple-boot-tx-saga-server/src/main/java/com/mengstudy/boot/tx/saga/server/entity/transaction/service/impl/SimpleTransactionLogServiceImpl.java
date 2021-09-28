package com.mengstudy.boot.tx.saga.server.entity.transaction.service.impl;

import com.mengstudy.boot.tx.saga.server.entity.transaction.entity.SimpleTransactionLog;
import com.mengstudy.boot.tx.saga.server.entity.transaction.mapper.SimpleTransactionLogMapper;
import com.mengstudy.boot.tx.saga.server.entity.transaction.service.ISimpleTransactionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gary Fu
 * @since 2021-09-28
 */
@Service
public class SimpleTransactionLogServiceImpl extends ServiceImpl<SimpleTransactionLogMapper, SimpleTransactionLog> implements ISimpleTransactionLogService {

}
