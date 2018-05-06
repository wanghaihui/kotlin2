//
// Created by conquer on 2018/4/1.
// 智能指针
//

template<typename T>
class SmartPointer {
// inline--能提高函数的执行效率--增强效率
/*inline SmartPointer : m_ptr(0) {

}*/
private:
    T * m_ptr;
};
