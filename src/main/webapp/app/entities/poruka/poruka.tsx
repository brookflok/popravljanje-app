import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './poruka.reducer';
import { IPoruka } from 'app/shared/model/poruka.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPorukaProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Poruka = (props: IPorukaProps) => {
  const [search, setSearch] = useState('');

  useEffect(() => {
    props.getEntities();
  }, []);

  const startSearching = () => {
    if (search) {
      props.getSearchEntities(search);
    }
  };

  const clear = () => {
    setSearch('');
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);

  const { porukaList, match, loading } = props;
  return (
    <div>
      <h2 id="poruka-heading">
        <Translate contentKey="popravljanjeApp.poruka.home.title">Porukas</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="popravljanjeApp.poruka.home.createLabel">Create new Poruka</Translate>
        </Link>
      </h2>
      <Row>
        <Col sm="12">
          <AvForm onSubmit={startSearching}>
            <AvGroup>
              <InputGroup>
                <AvInput
                  type="text"
                  name="search"
                  value={search}
                  onChange={handleSearch}
                  placeholder={translate('popravljanjeApp.poruka.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </AvGroup>
          </AvForm>
        </Col>
      </Row>
      <div className="table-responsive">
        {porukaList && porukaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.poruka.text">Text</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.poruka.datum">Datum</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.poruka.postoji">Postoji</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.poruka.dodatniInfoUser">Dodatni Info User</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.poruka.chat">Chat</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {porukaList.map((poruka, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${poruka.id}`} color="link" size="sm">
                      {poruka.id}
                    </Button>
                  </td>
                  <td>{poruka.text}</td>
                  <td>{poruka.datum ? <TextFormat type="date" value={poruka.datum} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{poruka.postoji ? 'true' : 'false'}</td>
                  <td>
                    {poruka.dodatniInfoUser ? (
                      <Link to={`dodatni-info-user/${poruka.dodatniInfoUser.id}`}>{poruka.dodatniInfoUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{poruka.chat ? <Link to={`chat/${poruka.chat.id}`}>{poruka.chat.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${poruka.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${poruka.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${poruka.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="popravljanjeApp.poruka.home.notFound">No Porukas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ poruka }: IRootState) => ({
  porukaList: poruka.entities,
  loading: poruka.loading,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Poruka);
