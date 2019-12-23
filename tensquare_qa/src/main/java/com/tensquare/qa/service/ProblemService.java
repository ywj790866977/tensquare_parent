package com.tensquare.qa.service;


import com.tensquare.qa.dao.ProblemDao;
import com.tensquare.qa.pojo.Problem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ProblemService {
    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private IdWorker idWorker;

    public Page<Problem> newlist(String labelId,int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.newlist(labelId,pageable);
    }

    public Page<Problem> hotlist(String labelId,int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.hotlist(labelId,pageable);
    }

    public Page<Problem> waitlist(String labelId,int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.waitlist(labelId,pageable);
    }


    public List<Problem> findAll(){
        return problemDao.findAll();
    }

    public Problem findById(String id){
        return problemDao.findById(id).get();
    }

    public void save(Problem problem){
        problem.setId(idWorker.nextId()+"");
        problemDao.save(problem);
    }

    public void update(Problem problem){
        problemDao.save(problem);
    }

    public void deleteById(String id){
        problemDao.deleteById(id);
    }

    /**
     * @Description: 条件查询
     * @param: label
     * @Return: java.util.List<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 16:52
     */
    public List<Problem> findSearch(Problem problem) {
        return problemDao.findAll(new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(problem.getTitle())){
                    list.add(criteriaBuilder.like(root.get("title").as(String.class),"%"+problem.getTitle()+"%"));
                }
                if(!StringUtils.isBlank(problem.getContent())){
                    list.add(criteriaBuilder.equal(root.get("content").as(String.class),problem.getContent()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        });
    }

    /**
     * @Description: 分页查询
     * @param: label
     * @param: page
     * @param: size
     * @Return: org.springframework.data.domain.Page<com.tensquare.base.pojo.Label>
     * @Date: 2019-12-20 17:12
     */
    public Page<Problem> pageQuery(Problem problem, int page, int size) {
        //添加分页
        Pageable pageable = PageRequest.of(page-1,size);
        //添加条件
        return problemDao.findAll(new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(problem.getTitle())){
                    list.add(criteriaBuilder.like(root.get("title").as(String.class),"%"+problem.getTitle()+"%"));
                }
                if(!StringUtils.isBlank(problem.getContent())){
                    list.add(criteriaBuilder.equal(root.get("content").as(String.class),problem.getContent()));
                }

                Predicate[] predicates = new Predicate[list.size()];
                predicates = list.toArray(predicates);

                return criteriaBuilder.and(predicates);
            }
        },pageable);

    }
}
