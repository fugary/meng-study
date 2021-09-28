package com.mengstudy.boot.tx.saga.server.entity.transaction.service.impl;

import com.mengstudy.boot.tx.saga.server.entity.transaction.entity.SimpleSubTransaction;
import com.mengstudy.boot.tx.saga.server.entity.transaction.mapper.SimpleSubTransactionMapper;
import com.mengstudy.boot.tx.saga.server.entity.transaction.service.ISimpleSubTransactionService;
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
public class SimpleSubTransactionServiceImpl extends ServiceImpl<SimpleSubTransactionMapper, SimpleSubTransaction> implements ISimpleSubTransactionService {

}
